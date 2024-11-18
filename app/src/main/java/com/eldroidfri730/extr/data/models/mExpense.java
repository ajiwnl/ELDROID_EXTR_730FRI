    package com.eldroidfri730.extr.data.models;

    import java.text.SimpleDateFormat;
    import java.util.Date;

    public class mExpense {
        private String id;
        private String name;
        private String category;
        private float amount;
        private Date date;
        private String desc;

        public mExpense(String name, String category, float amount, Date date, String desc) {
            this.name = name;
            this.category = category;
            this.amount = amount;
            this.date = date;
            this.desc = desc;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getId() {
            return id;
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

        public Date getDate() {
            return date;
        }

        public String getDesc() {
            return desc;
        }

        public String getFormattedDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(date);
        }
    }