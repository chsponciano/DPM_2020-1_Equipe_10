import React, { Component } from 'react';
import { Alert, CheckBox, FlatList, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { doGetAuth, doPostAuth } from '../utils/HttpUtils';
import LoadingComponent from './LoadingComponent';


export default class DeviceListComponent extends Component {
  constructor(props) {
    super(props);
    this.state = { loading: true };
  }

  async componentDidMount() {
    this.makeRemoteRequest();
  }

  async makeRemoteRequest() {
    this.setState({ loading: true });
    try {
      const response = await doGetAuth(`device`);
      const body = await response.json();
      this.setState({ data: body });
    } catch (e) {
      Alert.alert('Erro', e.message);
    }
    this.setState({ loading: false });
  };

  async changeUse(item, newValue) { 
    this.setState({ loading: true });
    item.use = newValue;
    try {
      await doPostAuth(`device/${item.id}/use`, { use: newValue });
    } catch (e) {
      Alert.alert('Erro', e.message);
    }
    this.setState({ loading: false });
  }

  render() {
    return this.state.loading ? this.renderLoading() : this.renderLoaded();
  }

  renderLoading() {
    return (
      <LoadingComponent/>
    );
  }

  renderLoaded() {
    return (
      <View style={styles.container}>
        <FlatList
          data={this.state.data}
          renderItem={({ item }) => (
            <View style={styles.listItem}>
              <CheckBox
                value={item.use}
                onValueChange={async value => this.changeUse(item, value)}
              />
              <TouchableOpacity onPress={() => this.props.navigation.navigate('DeviceFormComponent', { device: item, refreshDeviceList: async () => this.makeRemoteRequest() })}> 
              <View style={{ marginLeft: 10 }}>
                <Text>{item.description}</Text>
                <Text>{item.mac}</Text>
                <Text>{item.ip}</Text>
              </View>
              </TouchableOpacity>
            </View>
          )}
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    justifyContent: 'center',
    paddingTop: 30,
    paddingLeft: 10,
    paddingRight: 10,
  },
  listItem: {
    marginTop: 5,
    marginBottom: 5,
    flexDirection:'row',
    alignItems: 'center'
  }
});
