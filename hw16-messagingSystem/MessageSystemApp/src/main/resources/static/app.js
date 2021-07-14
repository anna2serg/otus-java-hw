let stompClient = null;

function init() {
  stompClient = Stomp.over(new SockJS('/app-websocket'));
  stompClient.connect({}, (frame) => {
    stompClient.subscribe('/topic/clients', (response) => setClients(JSON.parse(response.body)));
    stompClient.subscribe('/topic/clientById', (response) => setClient(JSON.parse(response.body)));
    stompClient.subscribe('/topic/client', () => showClients());
    stompClient.send("/app/clientList", {}, {});
  });
}

function setClient(client) {
  const clientDataContainer = document.getElementById('clientDataContainer');
  if (client.status === "SUCCESS") {
    clientDataContainer.innerHTML = JSON.stringify(client.result);
  } else {
    clientDataContainer.innerHTML = client.message;
  }
}

function getClientById() {
  const clientIdTextBox = document.getElementById('clientIdTextBox');
  const id = clientIdTextBox.value;
  stompClient.send("/app/client/" + id, {}, {});
}

function setClients(clients) {
  $('tbody').empty()
  if (clients.status === "SUCCESS") {
    for (let client of clients.result.data) {
      $('<tr><td rowspan='+ client.phones.length + '>' + client.id +
          '</td><td rowspan='+ client.phones.length + '>' + client.name +
          '</td><td rowspan='+ client.phones.length + '>' + client.address.street +
          '</td><td>' + client.phones[0].number +
          '</td></tr>').appendTo('tbody');
      let phoneNumber = 0;
      for (let phone of client.phones) {
        if (phoneNumber > 0) {
          $('<tr><td>' + phone.number + '</td></tr>').appendTo('tbody');
        }
        phoneNumber = phoneNumber + 1;
      }
    }
  } else {
    $('<tr><td colspan=4>' + clients.message + '</td></tr>').appendTo('tbody');
  }
}

function showClients() {
  window.location.href = '/clients';
  stompClient.send("/app/clientList", {}, {});
}

function addClientFromJson() {
  const clientJsonTextBox = document.getElementById('clientJson');
  const clientJsonText = clientJsonTextBox.value;
  stompClient.send("/app/client/add", {}, clientJsonText);
}

function addClient(event) {
  event.preventDefault();
  const clientName = document.getElementById('clientName').value;
  const clientAddress = document.getElementById('clientAddress').value;
  const clientPhonesEl = document.getElementById('clientPhones');
  const clientPhones = clientPhonesEl.getElementsByTagName('input');

  const phoneValues = [];
  for (let i = 0; i < clientPhones.length; i++) {
    phoneValues.push({"number": clientPhones[i].value});
  }

  const client = {
    "name": clientName,
    "phones": phoneValues,
    "address": {"street": clientAddress}
  }
  stompClient.send("/app/client/add", {}, JSON.stringify(client));
}

function addPhone(event) {
  event.preventDefault();
  const clientPhones = document.getElementById('clientPhones');
  const nextPhoneNum = clientPhones.getElementsByTagName('input').length + 1;
  clientPhones.innerHTML += "<label for=\"clientPhone" + nextPhoneNum + "\"" + ">Контактный телефон №" + nextPhoneNum + "</label>\n" +
                            "<input type=\"text\" id=\"clientPhone" + nextPhoneNum + "\" value=\"\"/>";


}