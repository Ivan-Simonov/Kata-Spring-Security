let userInfo = "http://localhost:8080/api/admin/info";
currentUser = fetch(userInfo).then((response) => response.json())
const urlAllUser = "http://localhost:8080/api/admin/users"
const urlPost = "http://localhost:8080/api/admin/add"
const urlPATCH = "http://localhost:8080/api/admin/update"
const urlDelete = "http://localhost:8080/api/admin/delete/"
const urlRole = "http://localhost:8080/api/admin/roles"
const listRoles = fetch(urlRole).then(response => response.json())
const editUserModal = new bootstrap.Modal(document.getElementById("editUserModal"))
const editId = document.getElementById("id_edit")
const editFirstName = document.getElementById("firstName_edit")
const editLastName = document.getElementById("lastName_edit")
const editAge = document.getElementById("age_edit")
const editEmail = document.getElementById("email_edit")
const editPassword = document.getElementById("password_edit")
const editRole = document.getElementById("role_edit")
const formEdit = document.getElementById("edit_user_form")
const deleteModalBtn = new bootstrap.Modal(document.getElementById("deleteUserModal"))

fetch(userInfo)
    .then((response) => {
        return response.json();
    })
    .then((data) => {
        document.getElementById("navbar-info").append(data.email + " with roles: " + data.roleNames);
    })

fetch(urlAllUser)
    .then((response) => {
        return response.json();
    })
    .then((users) => {
        let table = document.getElementById("table-list-users");
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

function generateUsersTable(table, data) {
    for (let element of data) {
        let row = table.insertRow();
        for (key in element) {
            let cell = row.insertCell();
            let text = document.createTextNode(element[key]);
            cell.appendChild(text);
        }
        let editButton = '<button type="button" class="btn btn-info btn-sm text-white" id="editUserBtn">Edit</button>'
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
        for (let i = 0; i < nameRole.options.length; i++) {
            if (nameRole.options[i].selected) {
                listRoles.push({
                    id: nameRole.options[i].value,
                    role: "ROLE_" + nameRole.options[i].innerHTML
                })
                roleValue += nameRole.options[i].innerHTML + ''
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
        })
        document.getElementById("userTable-tab").click()
    })

//модальное окно Edit
on(document, 'click', '#editUserBtn', e => {
    const fila = e.parentNode.parentNode
    let option = ''
    editId.value = fila.children[0].innerHTML
    let urlEditUser = "http://localhost:8080/api/admin/user/" + editId.value;
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

    for (let i = 0; i < nameRoleEdit.options.length; i++) {
        if (nameRoleEdit.options[i].selected) {
            listRoleEdit.push({id: nameRoleEdit.options[i].value, role: 'ROLE_' + nameRoleEdit.options[i].innerHTML})
            roleValueEdit += nameRoleEdit.options[i].innerHTML + ' '
        }
    }

    fetch(urlPATCH, {
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
})


let rowDelete = null
on(document, 'click', '#deleteUserBtn', e => {
    rowDelete = e.parentNode.parentNode
    let deleteId = rowDelete.children[0].innerHTML;
    let urlDeleteUser = "http://localhost:8080/api/admin/user/" + deleteId;
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