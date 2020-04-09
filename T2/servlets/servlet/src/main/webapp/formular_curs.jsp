<html xmlns:jsp="http://java.sun.com/JSP/Page">
<head>
    <title>Formular curs</title>
    <meta charset="UTF-8" />
</head>
<body>
<h3>Formular curs</h3>
Introduceti datele despre curs:
<form action="./add-course" method="post">
    Nume: <input type="text" name="nume" />
    <br />
    Universitate: <input type="text" name="universitate" />
    <br />
    Numar studenti: <input type="number" name="nrStudenti" />
    <br />
    <br />
    <button type="submit" name="submit">Trimite</button>
</form>
</body>
</html>
