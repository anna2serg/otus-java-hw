let stompClient = null;

function init() {
  stompClient = Stomp.over(new SockJS('/app-websocket'));
  stompClient.connect({}, (frame) => {
    stompClient.subscribe('/topic/clients', (response) => setClients(JSON.parse(response.body)));
    stompClient.subscribe('/topic/clientById', (response) => setClient(JSON.parse(response.body)));
    stompClient.subscribe('/topic/client', () => reload());
    stompClient.send("/app/clients", {}, {});
  });
}

function setClient(client) {
  const clientDataContainer = document.getElementById('clientDataContainer');
  clientDataContainer.innerHTML = JSON.stringify(client);
}

function getClientById() {
  const clientIdTextBox = document.getElementById('clientIdTextBox');
  const id = clientIdTextBox.value;
  stompClient.send("/app/client/" + id, {}, {});
}

function setClients(clients) {
  $('tbody').empty()
  for (let client of clients) {
    $('<tr><th scope="row">' + client.id + '</th><td>' + client.name + '</td></tr>').appendTo('tbody');
  }
}

function createClientFromJson() {
  const clientJsonTextBox = document.getElementById('clientJson');
  const clientJsonText = clientJsonTextBox.value;
  stompClient.send("/app/client/add", {}, clientJsonText);
}

function reload() {
  return location.reload();
}