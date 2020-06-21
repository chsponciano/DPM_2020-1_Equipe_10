import Geolocation from '@react-native-community/geolocation';
import React, { Component } from 'react';
import { Button, ScrollView, StyleSheet, View } from 'react-native';
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
    Geolocation.getCurrentPosition(async info => {
      const latitude = info.coords.latitude;
      const longitude = info.coords.longitude;
      await doPostAuth('device/checkLocation', { longitude, latitude });
      this.setState({ loading: false });
    });
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