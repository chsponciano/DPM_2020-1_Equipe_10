import { Formik } from 'formik';
import React, { Component } from 'react';
import { Alert, Button, StyleSheet, Text, TextInput, View } from 'react-native';
import { globalStyles } from '../styles/global.js';
import { doPostAuth } from '../utils/HttpUtils';
import LoadingComponent from './LoadingComponent.js';


export default class DeviceFormComponent extends Component {

  constructor(props) {
    super(props);
    this.state = { loading: false, device: this.props.route.params.device };
  }

  async save(values) {
    this.setState({ loading: true });
    try {
      await doPostAuth(`device/${this.state.device.id}/description`, values);
      this.props.route.params.refreshDeviceList();
      this.props.navigation.goBack();
    } catch(e) {
      console.log(e);
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
    )
  }

  renderLoaded() {
    return (
      <View style={styles.container}>
        <View style={ styles.titleView }>
          <Text style={ globalStyles.titleText }>{ this.state.device.mac }</Text>
        </View>
        <View style={ styles.titleView }>
          <Text style={ globalStyles.titleText }>{ this.state.device.ip }</Text>
        </View>
        <Formik
          style={styles.form}
          initialValues={{ description: this.state.device.description }}
          onSubmit={async (values) => this.save(values)}>
          {props => (
            <View>
              <TextInput
                style={globalStyles.input}
                placeholder='Descrição'
                onChangeText={props.handleChange('description')}
                value={props.values.description}
              />

              <View style={ globalStyles.viewBtn }>
                <Button color='maroon' title="Salvar alterações" onPress={props.handleSubmit} /> 
              </View>

              <View style={ globalStyles.viewBtn }>
                <Button color='maroon' title="Cancelar" onPress={() => this.props.navigation.goBack()} /> 
              </View>

            </View>
          )}
        </Formik>
      </View>
      
    );
  }
}

const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: '#fff',
      justifyContent: 'center',
    },
    titleView:{
      margin: 10
    }
});