    function drowTable() {
        fetch('http://localhost:8080/rest/api/client')
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                //console.log(data);
                $('#clients').append(
                    `<tbody>${data.map(n =>
                        `<tr>
                        <td>${n.id}</td>
                        <td>${n.phone}</td>
                        <td>${n.code_mobile_operator}</td>
                        <td>${n.tag}</td>
                        <td>${n.time_zone}</td>
                        <td class="text-center">
                            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
                                    data-target="#ModalEdit"
                                    data-id="${n.id}"
                                    data-phone="${n.phone}"
                                    data-code_mobile_operator="${n.code_mobile_operator}"
                                    data-tag="${n.tag}"
                                    data-time_zone="${n.time_zone}"
                                    >
                                Редактировать
                            </button>
                        </td>
                        <td class="text-center">
                            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
                                data-target="#ModalDelete"
                                data-id="${n.id}"
                                data-phone="${n.phone}"
                                data-code_mobile_operator="${n.code_mobile_operator}"
                                data-tag="${n.tag}"
                                data-time_zone="${n.time_zone}"
                                >
                            Удалить
                            </button>
                        </td>
                    </tr>`).join('')}
                </tbody>`
                );
            });
    };

    $(document).ready(function(){
        drowTable();
    });

    function myFetch(url, data, method) {
        try {
            fetch(url, {
                method: method,
                headers: {"Content-type": "application/json;"},
                body: data
            })
            .then((response) => {
                if (response.status === 200 ) {
                    if (method != 'DELETE') {
                        response.json().then(data => { console.log('Успех: ' + JSON.stringify(data) ) });
                    } else {
                        console.log('Запись удалена!');
                    }
                    // Очищаем таблицу.
                    $('#clients tbody').remove();
                    // Заполняем таблицу новыми данными.
                    drowTable();
                } else {
                    response.json().then(data => { console.log('Ошибка: ' + JSON.stringify(data) ) });
                }
            });
        } catch (error) {
            console.error('Ошибка:', error);
        }
    };

    $('#buttonEdit').on('click', function () {
        var data = $('#ModalEditForm').serialize();
        data = JSON.stringify(formToJson(data));
        var client = JSON.parse(data);
        const url = 'http://localhost:8080/rest/api/client/' + client.id;
        myFetch(url, data, 'PUT');
        // Скрываем модальное окно
        $(this).closest('#ModalEdit').modal('hide');
    });

    $('#buttonDelete').on('click', function () {
        var data = $('#ModalDeleteForm').serialize();
        data = JSON.stringify(formToJson(data));
        var client = JSON.parse(data);
        const url = 'http://localhost:8080/rest/api/client/' + client.id;
        myFetch(url, data, 'DELETE');
        // Скрываем модальное окно
        $(this).closest('#ModalDelete').modal('hide');
    });

    $('#buttonAdd').on('click', function () {
        var data = $('#ModalAddForm').serialize();
        data = JSON.stringify(formToJson(data));
        const url = 'http://localhost:8080/rest/api/client';
        myFetch(url, data, 'POST');
        // Скрываем модальное окно
        $(this).closest('#ModalAdd').modal('hide');
     });

    $('#ModalEdit').on('show.bs.modal', function (event) {
        const button = $(event.relatedTarget);
        const id = button.data('id');
        const phone = button.data('phone');
        const code_mobile_operator = button.data('code_mobile_operator');
        const tag = button.data('tag');
        const time_zone = button.data('time_zone');
        const modal = $(this);

        modal.find('#id').val(id);
        modal.find('#phone').val(phone);
        modal.find('#code_mobile_operator').val(code_mobile_operator);
        modal.find('#tag').val(tag);
        modal.find('#time_zone').val(time_zone);
    });

    $('#ModalAdd').on('show.bs.modal', function (event) {
        const button = $(event.relatedTarget);
        const modal = $(this);
    });

    $('#ModalAdd').on('hidden.bs.modal', function (event) {
        $(this).find("input,textarea,select").val('').end();
    });

    $('#ModalDelete').on('show.bs.modal', function (event) {
        const button = $(event.relatedTarget);
        const id = button.data('id');
        const modal = $(this);
        modal.find('#id').val(id);
    });

    function formToJson(url) {
        var hash;
        var myJson = {};
        var hashes = url.slice(url.indexOf('?') + 1 ).split('&');
        //console.log(hashes.length + ' ' + hashes);
        for (var i = 0; i < hashes.length; i++) {
            hash = hashes[i].split('=');
            hash[1] =  decodeURIComponent(hash[1]);
            //console.log(hash);
            myJson[hash[0]] = hash[1];
        }
        return myJson;
    }