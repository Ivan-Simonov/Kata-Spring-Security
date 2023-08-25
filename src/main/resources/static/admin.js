let userInfo = "http://localhost:8080/api/admin/info";
const urlAllUser = "http://localhost:8080/api/admin/users"
const urlSingleUser = "http://localhost:8080/api/admin/user/"
const urlPost = "http://localhost:8080/api/admin/add"
const urlPatch = "http://localhost:8080/api/admin/update"
const urlDelete = "http://localhost:8080/api/admin/delete/"
const urlRoles = "http://localhost:8080/api/admin/roles"
let editId = document.getElementById("id_edit")
let editFirstName = document.getElementById("firstName_edit")
let editLastName = document.getElementById("lastName_edit")
let editAge = document.getElementById("age_edit")
let editEmail = document.getElementById("email_edit")
let editPassword = document.getElementById("password_edit")
let editRole = document.getElementById("role_edit")
const listRoles = fetch(urlRoles).then(response => response.json())
const editUserModal = new bootstrap.Modal(document.getElementById("editUserModal"))
const formEdit = document.getElementById("edit_user_form")
const deleteModalBtn = new bootstrap.Modal(document.getElementById("deleteUserModal"))

fetch(userInfo)
    .then((response) => {
        return response.json();
    })
    .then((data) => {
        document.getElementById("navbar-info").append(data.email + " with roles: " + data.roleNames);
    })

fillUserTableData()

function fillUserTableData() {
    fetch(urlAllUser)
        .then((response) => {
            return response.json();
        })
        .then((users) => {
            let table = document.getElementById("table-list-users-body");
            let tableFiller = [];
            for (let user of users) {
                let userData = {
                    id: user.id,
                    firstName: user.firstName,
                    lastName: user.lastName,
                    age: user.age,
                    email: user.email,
                    roles: user.roleNames
                }
                tableFiller.push(userData);
            }
            generateUsersTable(table, tableFiller)
        })
}

function reloadUserTableData() {
    let table = document.getElementById("table-list-users-body");
    while (table.firstChild) {
        table.removeChild(table.lastChild);
    }
    fillUserTableData();
}

function generateUsersTable(table, data) {
    for (let element of data) {
        let row = table.insertRow();
        for (let key in element) {
            let cell = row.insertCell();
            let text = document.createTextNode(element[key]);
            cell.appendChild(text);
        }
        addButton(row.insertCell(), "Edit", "btn btn-sm btn-primary", "editUserBtn")
        addButton(row.insertCell(), "Delete", "btn btn-sm btn-danger", "deleteUserBtn")
    }
}

function addButton(parentElement, buttonName, buttonClassName, buttonId) {
    let button = document.createElement("input");
    button.id = buttonId;
    button.type = "button";
    button.name = buttonName;
    button.value = buttonName;
    button.className = buttonClassName;
    parentElement.appendChild(button);
}

const on = (element, event, selector, handler) => {
    element.addEventListener(event, e => {
        if (e.target.closest(selector)) {
            handler(e.target)
        }
    })
}

const fillRole = function (elementId) {
    listRoles.then(roles => {
        let result = ''
        for (let i in roles) {
            result += `<option value=${roles[i].id}>${roles[i].name.substring(5)}
                       </option>`
        }
        document.getElementById(elementId).innerHTML = result
    })
}

fillRole("role_select")

const newUserForm = document.getElementById("newUserForm")
document.getElementById("newUserForm")
    .addEventListener("submit", (e) => {
        e.preventDefault()
        let nameRole = document.getElementById("role_select")
        let listRoles = []
        let roleValue = ""

        for (let option of nameRole.options) {
            if (option.selected) {
                listRoles.push({
                    id: option.value,
                    role: "ROLE_" + option.innerHTML
                })
                roleValue += option.innerHTML + ''
            }
        }
        fetch(urlPost, {
            method: "POST",
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify({
                firstName: document.getElementById("firstName").value,
                lastName: document.getElementById("lastName").value,
                email: document.getElementById("email").value,
                age: document.getElementById("age").value,
                password: document.getElementById("password").value,
                roles: listRoles
            })
        }).then(() => {
            newUserForm.reset()
            reloadUserTableData()
            document.getElementById("userTable-tab").click()
        })
    })

on(document, 'click', '#editUserBtn', e => {
    const fila = e.parentNode.parentNode
    let option = ''
    editId.value = fila.children[0].innerHTML
    let urlEditUser = urlSingleUser + editId.value;
    fetch(urlEditUser, {
        method: "GET"
    }).then((response) => {
        return response.json()
    }).then((userData) => {
        editFirstName.value = userData.firstName;
        editLastName.value = userData.lastName;
        editAge.value = userData.age;
        editEmail.value = userData.email;
        editPassword.value = userData.password;
        listRoles.then(rolList => {
            rolList.forEach(role => {
                let selected = userData.roleNames.includes(role.name.substring(5)) ? 'selected' : ''
                option += `
                <option value="${role.id}" ${selected}>${role.name.substring(5)}</option>`

            })
            editRole.innerHTML = option
        })
    })
    editUserModal.show()
})

formEdit.addEventListener('submit', e => {
    e.preventDefault()
    let nameRoleEdit = document.getElementById("role_edit")
    let listRoleEdit = []
    let roleValueEdit = ''

    for (let option of nameRoleEdit.options) {
        if (option.selected) {
            listRoleEdit.push({id: option.value, role: 'ROLE_' + option.innerHTML})
            roleValueEdit += option.innerHTML + ' '
        }
    }

    fetch(urlPatch, {
        method: "PATCH",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: editId.value,
            firstName: editFirstName.value,
            lastName: editLastName.value,
            email: editEmail.value,
            age: editAge.value,
            password: editPassword.value,
            roles: listRoleEdit
        })
    })
    editUserModal.hide()
    location.reload()
})


let rowDelete = null
on(document, 'click', '#deleteUserBtn', e => {
    rowDelete = e.parentNode.parentNode
    let deleteId = rowDelete.children[0].innerHTML;
    let urlDeleteUser = urlSingleUser + deleteId;
    fetch(urlDeleteUser, {
        method: "GET"
    }).then((response) => {
        return response.json()
    }).then((userData) => {
        document.getElementById('id_delete').value = userData.id;
        document.getElementById('firstName_delete').value = userData.firstName;
        document.getElementById('lastName_delete').value = userData.lastName;
        document.getElementById('email_delete').value = userData.email;
        document.getElementById('age_delete').value = userData.age;
        let option = '';
        userData.roles.forEach(role => {
            option += `<option value="${role.id}">${role.name.substring(5)}</option>`
        })
        document.getElementById('role_delete').innerHTML = option;
    })
    deleteModalBtn.show()
})

document.getElementById('delete_user_form').addEventListener('submit', (e) => {
    e.preventDefault()
    fetch(urlDelete + rowDelete.children[0].innerHTML, {
        method: 'DELETE'
    }).then(() => {
        deleteModalBtn.hide()
        rowDelete.parentNode.removeChild(rowDelete)
    })
})