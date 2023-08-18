let userInfo = "http://localhost:8080/api/admin/info";
let allUserList = "http://localhost:8080/api/admin/users"

fetch(userInfo)
    .then((response) => {
        return response.json();
    })
    .then((data) => {
        document.getElementById("navbar-info").append(data.email + " with roles: " + data.roleNames);
    })

fetch(allUserList)
    .then((response) => {
        return response.json();
    })
    .then((users) => {
        let table = document.getElementById("table-list-users");
        let tableFiller = [];
        console.log(users[0]);
        for (let user of users) {
            let userData = {
                email: user.email,
                firstName: user.firstName,
                lastName: user.lastName,
                age: user.age,
                roles: user.roleNames
            }
            console.log(userData);
            tableFiller.push(userData);
        }
        generateUsersTable(table, tableFiller)
    })

function generateUsersTable(table, data) {
    for (let element of data) {
        let row = table.insertRow();
        for (key in element) {
            let cell = row.insertCell();
            let text = document.createTextNode(element[key]);
            cell.appendChild(text);
        }
        addButton(row.insertCell(), "Edit", "btn btn-sm btn-primary", data.id)
        addButton(row.insertCell(), "Delete", "btn btn-sm btn-danger", data.id)
    }
}

function addButton(parentElement, buttonName, buttonClassName, userId) {
    let button = document.createElement("input");
    button.type = "button";
    button.name = buttonName;
    button.value = buttonName;
    button.className = buttonClassName;
    button.click(function (){
        $("").modal();
    })
    button.click(function(){
        $("#myModal").modal();
    });
    parentElement.appendChild(button);
}
