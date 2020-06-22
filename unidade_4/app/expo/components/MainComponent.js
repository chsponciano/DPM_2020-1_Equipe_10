import Constants from 'expo-constants';
import * as Location from 'expo-location';
import React, { Component } from 'react';
import { Button, Platform, ScrollView, StyleSheet, View } from 'react-native';
import { globalStyles } from '../styles/global.js';
import { doPostAuth } from '../utils/HttpUtils';
import DeviceListComponent from './DeviceListComponent';
import LogListComponent from './LogListComponent';


export default class MainComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isDeviceList: true
    };

  }

  requestForCurrentLocation() {
    this.setState({ loading: true });
    
    if (Platform.OS === 'android' && !Constants.isDevice) {
      Alert.alert('Error',
        'Oops, this will not work on Sketch in an Android emulator. Try it on your device!'
      );
    } else {
      (async () => {
        let { status } = await Location.requestPermissionsAsync();
        if (status !== 'granted') {
          Alert.alert('Error', 'Permission to access location was denied');
        }

        let location = await Location.getCurrentPositionAsync({});
        const latitude = location.coords.latitude;
        const longitude = location.coords.longitude;
        try {
          await doPostAuth('device/checkLocation', { longitude, latitude });
        } catch (e) {
          console.log(e);
        }  
        await doPostAuth('device/checkLocation', { longitude, latitude });
        this.setState({ loading: false });
      })();
    }
      
  }


  render() {
    return this.state.isDeviceList ? this.renderDeviceList() : this.renderLogList();
  }

  renderDeviceList() {
    return (
      <View style={styles.container}>
        <ScrollView>
          <DeviceListComponent navigation={ this.props.navigation }></DeviceListComponent>
        </ScrollView>
        <View style={ globalStyles.viewBtn }>
          <Button color='maroon' title="Vizualizar logs" onPress={() => this.setState({ isDeviceList: false }) } /> 
        </View>
        <View style={ globalStyles.viewBtn }>
          <Button color='maroon' title="Ativar dispositivos próximos" onPress={() => this.requestForCurrentLocation() } /> 
        </View>
      </View>
    );
  }

  renderLogList() {
    return (
      <View style={styles.container}>
        <ScrollView>
          <LogListComponent navigation={ this.props.navigation }></LogListComponent>
        </ScrollView>
        <View style={ globalStyles.viewBtn }>
          <Button color='maroon' title="Vizualizar dispositivos" onPress={() => this.setState({ isDeviceList: true }) } /> 
        </View>
        <View style={ globalStyles.viewBtn }>
          <Button color='maroon' title="Ativar dispositivos próximos" onPress={() => this.requestForCurrentLocation() } /> 
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: '#fff',
      justifyContent: 'center',
    }
});