Account API Endpoints
All endpoints are prefixed with /accounts.

1. Create Account
POST /accounts
Params:
owner (String, required)
openingBalance (Double, optional)
Description: Creates a new account for the specified owner. If openingBalance is not provided, defaults to 0.0.
2. Deposit Funds
POST /accounts/{id}/deposit
Params:
amount (Double, required)
Description: Deposits the specified amount into the account with the given ID.
3. Withdraw Funds
POST /accounts/{id}/withdraw
Params:
amount (Double, required)
Description: Withdraws the specified amount from the account. Fails if balance would go below 0.0.
4. Delete Account
POST /accounts/{id}/delete
Description: Deletes the account with the given ID.
5. Get Account Balance
GET /accounts/{id}/balance
Description: Returns the current balance of the account.
6. List Accounts
GET /accounts
Description: Returns a list of all accounts.
7. Rename Account Owner
POST /accounts/{id}/rename
Params:
newOwner (String, required)
Description: Changes the owner name of the account.
Validation Rules
Owner names cannot be null or empty.
Transaction amounts must be at least 0.01.
Account balance cannot go below 0.0.
