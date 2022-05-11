    function drowTable() {
        fetch('http://localhost:8080/rest/api/notification')
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                //console.log(data);
                $('#notifications').append(
                    `<tbody>${data.map(n =>
                        `<tr>
                        <td>${n.id}</td>
                        <td>${n.start_notification}</td>
                        <td>${n.end_notification}</td>
                        <td>${n.filters.map(f => (f.key == 'code_mobile_operator') ? 'Code <b>' + f.value + '</b>' : 'Tag <b>' + f.value + '</b>' )}</td>
                        <td>${n.message}</td>
                        <td class="text-center">
                            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
                                    data-target="#ModalEdit"
                                    data-id="${n.id}"
                                    data-start_notification="${n.start_notification}"
                                    data-end_notification="${n.end_notification}"
                                    data-filters=${JSON.stringify(n.filters)}
                                    data-message="${n.message}"
                                    >
                                Редактировать
                            </button>
                        </td>
                        <td class="text-center">
                            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
                                data-target="#ModalDelete"
                                data-id="${n.id}"
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
                        $('#notifications tbody').remove();
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
            var n = JSON.parse(data);
            //delete n.code_mobile_operator;
            //delete n;

            notification_new = '{"id":' + n.id + ',' +
                                '"start_notification":"' +  n.start_notification + '",' +
                                '"end_notification":"' +  n.end_notification + '",' +
                                '"message":"' +  n.message + '",' +
                                '"filters":[' +  n.code_mobile_operator + ',' + n.tag +
                                ']}';

            data = notification_new;
            const url = 'http://localhost:8080/rest/api/notification/' + n.id;



            myFetch(url, data, 'PUT');
            // Скрываем модальное окно
            $(this).closest('#ModalEdit').modal('hide');
        });

        $('#buttonAdd').on('click', function () {
            var data = $('#ModalAddForm').serialize();
            data = JSON.stringify(formToJson(data));
            console.log(data);
            var n = JSON.parse(data);
            //delete n.code_mobile_operator;
            //delete n;

            notification_new = '{"start_notification":"' +  n.start_notification + '",' +
                                '"end_notification":"' +  n.end_notification + '",' +
                                '"message":"' +  n.message + '",' +
                                '"filters":[' +  n.code_mobile_operator_add + ',' + n.tag_add +
                                ']}';
            console.log(notification_new);

            data = notification_new;
            const url = 'http://localhost:8080/rest/api/notification';
            myFetch(url, data, 'POST');
            // Скрываем модальное окно
            $(this).closest('#ModalAdd').modal('hide');
        });

        $('#buttonDelete').on('click', function () {
            var data = $('#ModalDeleteForm').serialize();
            data = JSON.stringify(formToJson(data));
            var notification = JSON.parse(data);
            const url = 'http://localhost:8080/rest/api/notification/' + notification.id;
            myFetch(url, data, 'DELETE');
            // Скрываем модальное окно
            $(this).closest('#ModalDelete').modal('hide');
        });

        $('#ModalEdit').on('show.bs.modal', function (event) {
            const button = $(event.relatedTarget);
            const id = button.data('id');
            const start_notification = button.data('start_notification');
            const end_notification = button.data('end_notification');
            var filters = button.data('filters');

            if (filters[0].key == 'code_mobile_operator'){
                code_mobile_operator = filters[0];
                tag = filters[1];
            } else {
                code_mobile_operator = filters[1];
                tag = filters[0];
            }
            const message = button.data('message');
            const modal = $(this);

            modal.find('#id').val(id);
            modal.find('#start_notification').val(start_notification);
            modal.find('#end_notification').val(end_notification);
            modal.find('#message').val(message);

            //SelectCodeMobile = document.getElementById('code_mobile_operator');
            var SelectCodeMobile = $("#code_mobile_operator");
            SelectCodeMobile.empty();
            SelectCodeMobile.append($("<option></option>").val("").html("Выберите код оператора"));
            fetch('http://localhost:8080/rest/api/filter/code_mobile_operator')
                        .then((response) => {
                            return response.json();
                        })
                        .then((data) => {
                            data.map(n => {
                                if (code_mobile_operator.value != n.value) {
                                    SelectCodeMobile.append($("<option></option>").val('{"id":' + n.id + ',"key":"'+ n.key + '","value":"' + n.value + '"}').html(n.value));
                                } else {
                                    SelectCodeMobile.append($("<option selected='selected'></option>").val('{"id":' + n.id + ',"key":"'+ n.key + '","value":"' + n.value + '"}').html(n.value));
                                }
                            })
                        });

            var SelectTag = $("#tag");
            SelectTag.empty();
            SelectTag.append($("<option></option>").val("").html("Выберите метку"));
            fetch('http://localhost:8080/rest/api/filter/tag')
                        .then((response) => {
                            return response.json();
                        })
                        .then((data) => {
                            data.map(n => {
                                if (tag.value != n.value) {
                                    SelectTag.append($("<option></option>").val('{"id":' + n.id + ',"key":"'+ n.key + '","value":"' + n.value + '"}').html(n.value));
                                } else {
                                    SelectTag.append($("<option selected='selected'></option>").val('{"id":' + n.id + ',"key":"'+ n.key + '","value":"' + n.value + '"}').html(n.value));
                                }
                            })
                        });
        });

        $('#ModalAdd').on('show.bs.modal', function (event) {
            const button = $(event.relatedTarget);
            const modal = $(this);

            var SelectCodeMobile = $("#code_mobile_operator_add");
            SelectCodeMobile.empty();
            SelectCodeMobile.append($("<option></option>").val("").html("Выберите код оператора"));
            fetch('http://localhost:8080/rest/api/filter/code_mobile_operator')
                        .then((response) => {
                            return response.json();
                        })
                        .then((data) => {
                            data.map(n => {
                                SelectCodeMobile.append($("<option></option>").val('{"id":' + n.id + ',"key":"'+ n.key + '","value":"' + n.value + '"}').html(n.value));
                            })
                        });

            var SelectTag = $("#tag_add");
            SelectTag.empty();
            SelectTag.append($("<option></option>").val("").html("Выберите метку"));
            fetch('http://localhost:8080/rest/api/filter/tag')
                        .then((response) => {
                            return response.json();
                        })
                        .then((data) => {
                            data.map(n => {
                                SelectTag.append($("<option></option>").val('{"id":' + n.id + ',"key":"'+ n.key + '","value":"' + n.value + '"}').html(n.value));
                            })
                        });

        });

        $('#ModalAdd').on('hidden.bs.modal', function (event) {
            $(this).find("input,textarea,select").val('').end();
        });

        $('#ModalEdit').on('hidden.bs.modal', function (event) {
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