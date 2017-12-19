package com.papkovskaya.olga.pets.data;

import android.provider.BaseColumns;

/**
 * Created by olga on 19.12.17.
 */

public class PetsListContract {


    public static final class PetsListEntry implements BaseColumns {
        public static final String TABLE_NAME = "petslist";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_IMAGE = "image";
    }
}
