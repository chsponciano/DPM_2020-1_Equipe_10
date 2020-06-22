import React, { Component } from 'react';
import { Alert, FlatList, StyleSheet, Text, View } from 'react-native';
import { doGetAuth } from '../utils/HttpUtils';
import LoadingComponent from './LoadingComponent';


export default class LogListComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      loading: true,
      data: [],
    };
  }

  componentDidMount() {
    this.makeRemoteRequest();
  }

  async makeRemoteRequest() {
    this.setState({ loading: true });
    try {
      const response = await doGetAuth(`log`);
      const body = await response.json();
      this.setState({ data: body });
    } catch (e) {
      Alert.alert('Erro', e.message);
    }
    this.setState({ loading: false });
  };

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
            <Text>{item.log}</Text>
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
  }
});
