package ru.base.game.engine.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

interface Parser {
    Command.Instance[] parse();

    final class Default implements Parser {
        private final Lexer lexer;
        private Token token;

        public Default(String text) {
            this.lexer = new Lexer(text);
        }

        private Token current() {
            if (token == null) {
                token = lexer.next().orElse(null);
            }
            return token;
        }

        private void next() {
            token = lexer.next().orElse(null);
        }

        private void reset() {
            token = null;
            lexer.reset();
        }

        @Override
        public Command.Instance[] parse() {
            reset();
            List<Command.Instance> instructions = new ArrayList<>();
            if (current() != null) {
                do {
                    Token current = current();
                    if (current.type == TokenType.IDENTIFIER) {
                        instructions.add(parseInstruction(instructions.size()));
                    } else if (current.type == TokenType.SEMICOLON) {
                        next();
                    } else if (current.type == TokenType.RBRACE) {
                        break;
                    }
                } while (token != null);
            }
            return instructions.toArray(new Command.Instance[0]);
        }

        private Command.Instance parseInstruction(int index) {
            var identifier = current();
            List<Argument> arguments = new ArrayList<>();
            next();
            if (token != null && token.type == TokenType.LBRACE) {
                parseArguments(arguments);
            }
            return new Command.Instance(index, Command.of(identifier.lexeme), arguments.toArray(new Argument[0]));
        }

        private void parseArguments(List<Argument> arguments) {
            next();
            while (token != null && token.type != TokenType.RBRACE) {
                if (token.type == TokenType.COMMA) {
                    next();
                }
                arguments.add(parseArgument());
            }
            next();
        }

        private Argument parseArgument() {
            Argument argument = null;
            if (token != null && token.type == TokenType.NUMBER) {
                argument = new Argument.Numeric(Integer.parseInt(token.lexeme));
                next();
            } else if (token != null && token.type == TokenType.IDENTIFIER) {
                argument = new Program.Default(parse());
            } else if (token != null && token.type == TokenType.COMMA) {
                argument = new Argument.None();
            } else if (token != null && token.type == TokenType.RBRACE) {
                argument = new Argument.None();
            }

            if (argument == null) {
                throw new UnsupportedOperationException();
            }
            return argument;
        }

        @Override
        public String toString() {
            return token == null ? "<EOF>" : "" + token;
        }

        enum TokenType {
            NUMBER, IDENTIFIER, LBRACE, RBRACE, COMMA, SEMICOLON, UNKNOWN
        }

        record Token(TokenType type, String lexeme) {
            @Override
            public String toString() {
                return lexeme;
            }
        }

        private static final class Lexer {
            private final char[] chars;
            private int index;

            private Lexer(String text) {
                this.chars = text.trim().toLowerCase(Locale.ROOT).toCharArray();
            }

            private void reset() {
                index = 0;
            }

            private Optional<Token> next() {
                if (hasNext()) {
                    switch (chars[index]) {
                        case '{': {
                            index++;
                            return Optional.of(new Token(TokenType.LBRACE, "{"));
                        }
                        case '}': {
                            index++;
                            return Optional.of(new Token(TokenType.RBRACE, "}"));
                        }
                        case ',': {
                            index++;
                            return Optional.of(new Token(TokenType.COMMA, ","));
                        }
                        case ';': {
                            index++;
                            return Optional.of(new Token(TokenType.SEMICOLON, ";"));
                        }
                        default: {
                            if (Character.isAlphabetic(chars[index])) {
                                return parseIdentifier();
                            } else if (Character.isDigit(chars[index])) {
                                return parseNumber();
                            }
                            Token token = new Token(TokenType.UNKNOWN, "" + chars[index]);
                            ++index;
                            return Optional.of(token);
                        }
                    }
                }
                return Optional.empty();
            }

            private Optional<Token> parseNumber() {
                StringBuilder number = new StringBuilder();
                while (index < chars.length) {
                    if (Character.isDigit(chars[index])) {
                        number.append(chars[index]);
                    } else {
                        break;
                    }
                    index++;
                }
                return Optional.of(new Token(TokenType.NUMBER, number.toString()));
            }

            private Optional<Token> parseIdentifier() {
                StringBuilder text = new StringBuilder();
                while (index < chars.length) {
                    char c = chars[index];
                    if (Character.isAlphabetic(c) || Character.isDigit(c)) {
                        text.append(c);
                    } else {
                        break;
                    }
                    index++;
                }
                return Optional.of(new Token(TokenType.IDENTIFIER, text.toString()));
            }

            private boolean hasNext() {
                return index < chars.length;
            }
        }
    }
}
