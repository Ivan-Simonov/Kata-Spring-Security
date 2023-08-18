let url = "http://localhost:8080/api/user";

fetch(url)
    .then((response) => {
        return response.json();
    })
    .then((data) => {
        document.getElementById("navbar-info").append(data.email + " with roles: " + data.roleNames);
        let userInfo = {
            id: data.id,
            email: data.email,
            age: data.age,
            roles: data.roleNames
        }
        let table = document.getElementById("user-information-table")
        let tableFiller = [];
        tableFiller.push(userInfo);
        generateTable(table, tableFiller)
        if (!data.roleNames.includes("ADMIN")) {
            document.getElementById("sidebar-admin-link").style.display = 'none';
        }
    })

function generateTable(table, data) {
    for (let element of data) {
        let row = table.insertRow();
        for (key in element) {
            let cell = row.insertCell();
            let text = document.createTextNode(element[key]);
            cell.appendChild(text);
        }
    }
}