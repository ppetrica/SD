<html>
    <head>
        <title>Main page</title>
    </head>
    <body>
        <h1>Hello, homework.</h1>
        <a href="students.jsp">Registered students page</a>
        <hr/>
        <h1>Search for students</h1>
        <form action="./search.jsp" method="get">
            <label for="criteria">Search criteria:</label>

            <select id="criteria" name="criteria">
                <option value="firstName">First Name</option>
                <option value="lastName">Last Name</option>
                <option value="age">Age</option>
            </select>
            <label>
                <input type="text" name="value"/>
            </label>
            <input type="submit" value="Search"/>
        </form>
        <hr/>
    </body>
</html>
