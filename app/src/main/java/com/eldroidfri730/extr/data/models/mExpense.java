    package com.eldroidfri730.extr.data.models;

    import androidx.annotation.Nullable;

    import java.text.SimpleDateFormat;
    import java.util.Date;

    public class mExpense {
        private String name;
        private String category;
        private float amount;
        private Date date;
        @Nullable
        private String desc;

        public mExpense(String name, String category, float amount, Date date, String desc) {
            this.name = name;
            this.category = category;
            this.amount = amount;
            this.date = date;
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public float getAmount() {
            return amount;
        }

        public String getDesc() {
            return desc;
        }

        public String getFormattedDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(date);
        }

    }