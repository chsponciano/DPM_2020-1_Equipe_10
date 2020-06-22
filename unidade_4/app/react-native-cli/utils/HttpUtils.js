import { AsyncStorage } from 'react-native';

const api = 'http://10.0.2.2:8080';

export async function doPost(path, body) {
  const headers = { 'Content-Type': 'application/json; charset=utf8' };
  const url = `${api}/${path}`;
  console.log(url);
  return fetch(url, { method: 'POST', body: JSON.stringify(body), headers });
}

export async function doPostAuth(path, body) {
  const token = await AsyncStorage.getItem('token');
  const headers = { authorization: `Bearer ${token}`, 'Content-Type': 'application/json; charset=utf8' };
  const url = `${api}/${path}`;
  console.log(url);
  return fetch(url, { method: 'POST', body: JSON.stringify(body), headers });
}


export async function doGetAuth(path) {
  const token = await AsyncStorage.getItem('token');
  const headers = { authorization: `Bearer ${token}`, 'Content-Type': 'application/json; charset=utf8' };
  const url = `${api}/${path}`;
  console.log(url);
  return fetch(url, { method: 'GET', headers });
}
