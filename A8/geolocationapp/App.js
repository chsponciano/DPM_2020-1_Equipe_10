import React, { useEffect, useState } from 'react';
import { View, StyleSheet, Text, PermissionsAndroid } from 'react-native';

import Geolocation from 'react-native-geolocation-service';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    justifyContent: "center",
    alignItems: "center"
  },
  text: {
    color: 'black',
    fontSize: 30,
  },
});

export default function App() {
  const [hasLocationPermission, setHasLocationPermission] = useState(false);
  const [userPosition, setUserPosition] = useState(false);

  async function verifyLocationPermission() {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION
      );
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('permissão concedida');
        setHasLocationPermission(true);
      } else {
        console.error('permissão negada');
        setHasLocationPermission(false);
      }
    } catch (err) {
      console.warn(err);
    }
  }

  useEffect(() => {
    verifyLocationPermission();

    if (hasLocationPermission) {
      Geolocation.getCurrentPosition(
        position => {
          setUserPosition({
            latitude: position.coords.latitude,
            longitude: position.coords.longitude,
          });
        },
        error => {
          console.log(error.code, error.message);
        }
      );
    }
  }, [hasLocationPermission]);

  return (
    <View style={styles.container}>
      <Text style={styles.text}>Latitude: {userPosition.latitude}</Text>
      <Text style={styles.text}>Longitude: {userPosition.longitude}</Text>
    </View>
  );
}