# YuxinBookstore
Database Course Project

### Relative Page
Online Demo: [http://chenyux.in/bookstore](http://chenyux.in/bookstore)

Bookstore Frontend: [http://github.com/Orthocenter/BookstoreFrontend]ttp://github.com/Orthocenter/BookstoreFrontend)

Bookstore Backend: [http://github.com/Orthocenter/BookstoreBackend](http://github.com/Orthocenter/BookstoreBackend)

## Build & Run
```
cd src
javac YuxinBookstore/*.java
java -cp ./mysql.jar:. YuxinBookstore.Bookstore
```
## Preview
Since font width of each character is not equal on Github, some of following tables may look strange.

### Welcome Page
```
   ___             _        _
  / __\ ___   ___ | | _____| |_ ___  _ __ ___
 /__\/// _ \ / _ \| |/ / __| __/ _ \| '__/ _ \
/ \/  \ (_) | (_) |   <\__ \ || (_) | | |  __/
\_____/\___/ \___/|_|\_\___/\__\___/|_|  \___|

     Yuxin Chen  13307103248  May 19, 2015
  -------------------------------------------
 |  0  |            For customer             |
 |  1  |          For administrator          |
  -------------------------------------------
Please enter your choice (-1 for return) :
```

### Customer's Menu
```
  ----------------------------------------------------------
 |  0  |                 Simple book search                 |
 |  1  |                  Advanced Search                   |
 |  2  |       Record your feedback for a given book        |
 |  3  |             Assess a feed back record              |
 |  4  | Declare other users as 'trusted' or 'not-trusted'  |
 |  5  |          Show feedbacks for a given book           |
 |  6  |       Show all orders for a certain customer       |
 |  7  |            Show all items in your cart             |
 |  8  |                 Confirm your order                 |
  ----------------------------------------------------------
Please enter your choice (-1 for return) :
```

### Administrator's Menu
```
  --------------------------------------------------------------------
 |  0  |                          Add a book                          |
 |  1  |                       Add a new author                       |
 |  2  |                     Add a new publisher                      |
 |  3  |                    Arrival of more copies                    |
 |  4  |   Determine the degree of separation of two given authors    |
 |  5  |         Show most popular books in a certain period          |
 |  6  |       Show most popular publishers in a certain period       |
 |  7  |        Show most popular authors in a certain period         |
 |  8  |             Print the top m most 'trusted' users             |
 |  9  |             Print the top m most 'useful' users              |
  --------------------------------------------------------------------
Please enter your choice (-1 for return) :
```

### Book Search Result
```
  --------------------------------------------------------------------------------------------------------------------------
 |     |             title              |      price      |         one of authors         |              ISBN              |
 |==========================================================================================================================|
 |  0  |           解忧杂货店           |      39.5       |          (日)东野圭吾          |         9787544270878          |
 |  1  |             从0到1             |       45        |           彼得•蒂尔            |         9787508649719          |
  --------------------------------------------------------------------------------------------------------------------------
Please enter your choice (-1 for return) :
```

### Details of a book
```
  ---------------------------------------------------------------------------------------------------------------------------------------------
 |  0  |             Title              |                                              解忧杂货店                                              |
 |  1  |           Publisher            |                                             南海出版公司                                             |
 |  2  |          Publish date          |                                              2014-05-01                                              |
 |  3  |             Format             |                                                 精装                                                 |
 |  4  |             Price              |                                                 39.5                                                 |
 |  5  |             copies             |                                                 100                                                  |
 |  6  |            summary             | 现代人内心流失的东西，这家杂货店能帮你找回—— 僻静的街道旁有一家杂货店，只要写下烦恼投进卷帘门的投... |
  ---------------------------------------------------------------------------------------------------------------------------------------------
  --------------------------------------
 |     |             Author             |
 |======================================|
 |  0  |          (日)东野圭吾          |
  --------------------------------------
  ----------------------------------------------------------
 |  0  |                Add it into the cart                |
 |  1  |           Give you some suggested books            |
 |  2  |          Record your feedback for a book           |
 |  3  |        Show feedbacks related to this book         |
  ----------------------------------------------------------
```
