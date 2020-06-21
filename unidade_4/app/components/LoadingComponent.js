import React from 'react';
import {
  ActivityIndicator,
  StyleSheet,
  View,
} from 'react-native';


export default function LoadingComponent() {
  return (
    <View style={styles.horizontal}>
      <ActivityIndicator size="large" color="#0000ff" />
    </View>
  );
}

const styles = StyleSheet.create({
  horizontal: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    padding: 50,
  },
});