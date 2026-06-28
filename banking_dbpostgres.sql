
CREATE TABLE "users"(
    "id" UUID NOT NULL,
    "username" VARCHAR(50) NOT NULL,
    "password_hash" VARCHAR(255) NOT NULL,
    "email" VARCHAR(100) NOT NULL,
    "role" VARCHAR(20) NOT NULL, 
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE "customers"(
    "id" UUID NOT NULL, 
    "first_name" VARCHAR(50) NOT NULL,
    "last_name" VARCHAR(50) NOT NULL,
    "dob" DATE NOT NULL,
    "address" TEXT NOT NULL,
    "gender" VARCHAR(10) NOT NULL
);

CREATE TABLE "accounts"(
    "id" UUID NOT NULL,
    "customer_id" UUID NOT NULL,
    "account_number" VARCHAR(12) NOT NULL,
    "account_type" VARCHAR(20) NOT NULL,
    "currency" VARCHAR(3) NOT NULL,      
    "balance" DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    "status" VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', 
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);


CREATE TABLE "transactions"(
    "id" UUID NOT NULL,
    "transaction_reference" VARCHAR(50) NOT NULL,
    "from_account_id" UUID NULL, 
    "to_account_id" UUID NULL,  
    "amount" DECIMAL(15, 2) NOT NULL,
    "currency" VARCHAR(3) NOT NULL,
    "transaction_type" VARCHAR(20) NOT NULL, 
    "status" VARCHAR(20) NOT NULL DEFAULT 'PENDING', 
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE "qr_payments"(
    "id" UUID NOT NULL,
    "merchant_name" VARCHAR(100) NOT NULL,
    "qr_code_data" TEXT NOT NULL,
    "transaction_id" UUID NOT NULL,
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE "notification"(
    "id" UUID NOT NULL,
    "user_id" UUID NOT NULL,
    "title" VARCHAR(100) NOT NULL,
    "message" TEXT NOT NULL,
    "notification_type" VARCHAR(20) NOT NULL, 
    "is_read" BOOLEAN NOT NULL DEFAULT FALSE,
    "sent_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

ALTER TABLE "users" ADD PRIMARY KEY("id");
ALTER TABLE "users" ADD CONSTRAINT "users_username_unique" UNIQUE("username");
ALTER TABLE "users" ADD CONSTRAINT "users_email_unique" UNIQUE("email");

ALTER TABLE "customers" ADD PRIMARY KEY("id");

ALTER TABLE "accounts" ADD PRIMARY KEY("id");
ALTER TABLE "accounts" ADD CONSTRAINT "accounts_account_number_unique" UNIQUE("account_number");

ALTER TABLE "transactions" ADD PRIMARY KEY("id");
ALTER TABLE "transactions" ADD CONSTRAINT "transactions_reference_unique" UNIQUE("transaction_reference");

ALTER TABLE "qr_payments" ADD PRIMARY KEY("id");
ALTER TABLE "qr_payments" ADD CONSTRAINT "qr_payments_transaction_id_unique" UNIQUE("transaction_id"); 

ALTER TABLE "notification" ADD PRIMARY KEY("id");

ALTER TABLE "customers" ADD CONSTRAINT "customers_id_foreign" FOREIGN KEY("id") REFERENCES "users"("id") ON DELETE CASCADE;

ALTER TABLE "accounts" ADD CONSTRAINT "accounts_customer_id_foreign" FOREIGN KEY("customer_id") REFERENCES "customers"("id");

ALTER TABLE "transactions" ADD CONSTRAINT "transactions_from_account_id_foreign" FOREIGN KEY("from_account_id") REFERENCES "accounts"("id");
ALTER TABLE "transactions" ADD CONSTRAINT "transactions_to_account_id_foreign" FOREIGN KEY("to_account_id") REFERENCES "accounts"("id");

ALTER TABLE "qr_payments" ADD CONSTRAINT "qr_payments_transaction_id_foreign" FOREIGN KEY("transaction_id") REFERENCES "transactions"("id") ON DELETE CASCADE;

ALTER TABLE "notification" ADD CONSTRAINT "notification_user_id_foreign" FOREIGN KEY("user_id") REFERENCES "users"("id") ON DELETE CASCADE;

ALTER TABLE "accounts" ADD CONSTRAINT "chk_balance_not_negative" CHECK ("balance" >= 0);


ALTER TABLE "transactions" ADD CONSTRAINT "chk_amount_above_zero" CHECK ("amount" > 0);



CREATE INDEX "idx_transactions_from_account" ON "transactions"("from_account_id");
CREATE INDEX "idx_transactions_to_account" ON "transactions"("to_account_id");
CREATE INDEX "idx_accounts_customer_id" ON "accounts"("customer_id");
CREATE INDEX "idx_notification_user_id" ON "notification"("user_id");