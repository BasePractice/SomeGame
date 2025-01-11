package ru.base.game.engine;

public class BadWerds {

    private int Matugalnik() {
        String[] BadWerds = {"ЖёвыныйКрот!!", "Божечкикошечки та за шоооооо :::(", "Да сколько можно бродить по этим лабиринтам?!",
            "@%*№#*&!!!!", "Непереводимая игра слов", "verdammtes schwein auf putta!", "Ну всё, я выпускаю КРАКЕНА!", "Приключенье на 5 минут ага...", "bringin sie aus dus FLUGGEGEHAIMHEN!!"
            , "Это хуже чем когда я пытался стать программистом...", "Лучше б доучился на программиста..."};
        int badWerdindex = BadWerds.length;
        int randWerd = (int) (Math.random() * badWerdindex);
    return randWerd;
    }
    }

