import Geolocation from '@react-native-community/geolocation';
import { Formik } from 'formik';
import React, { Component } from 'react';
import { Alert, AsyncStorage, Button, StyleSheet, Text, TextInput, View } from 'react-native';
import BackgroundTask from 'react-native-background-task';
import { connect } from 'react-redux';
import { globalStyles } from '../styles/global.js';
import { doPost, doPostAuth } from '../utils/HttpUtils';
import LoadingComponent from './LoadingComponent';


export class LoginComponent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false
    }
  }

  async makeLoginAttempt(values) {
    this.setState({ loading: true });
    try {
      const response = await doPost('auth/login', values);
      const body = await response.json();
      if (body.success) {
        await AsyncStorage.setItem('token', body.token);
        this.props.login({ type: 'LOGIN' });
        setInterval(() => {
          Geolocation.getCurrentPosition(async info => {
            const latitude = info.coords.latitude;
            const longitude = info.coords.longitude;
            try {
              await doPostAuth('device/checkLocation', { longitude, latitude });
            } catch (e) {
              console.log(e);
            }
          });
        }, 60000);
      } else {
        Alert.alert('Falha ao realizar login', 'Usuário e/ou senha inválidos!');
      }
      this.setState({ loading: false });
    } catch (e) {
      Alert.alert('Falha ao realizar login', e.message);
      this.setState({ loading: false });
    }
    
    
  }

  scheduleGeoBackgroundTask() {
    BackgroundTask.schedule({
      period: 60, // Roda a cada 60 segundos
    });
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

      <View style={ styles.titleView }>
        <Text style={ globalStyles.titleText }>{'DPM'}</Text>
      </View>
        <Formik
          style={styles.form}
          initialValues={{ username: '', password: '' }}
          onSubmit={async (values) => this.makeLoginAttempt(values)}
        >
        {props => (
          <View>
            <TextInput
              style={globalStyles.input}
              placeholder='Nome de usuário'
              onChangeText={props.handleChange('username')}
              value={props.values.username}
            />

            <TextInput
              style={globalStyles.input}
              secureTextEntry={true}
              placeholder='Senha'
              onChangeText={props.handleChange('password')}
              value={props.values.password}
            />

            <View style={ globalStyles.viewBtn }>
              <Button color='maroon' title="Fazer login" onPress={ props.handleSubmit } /> 
            </View>

            <View style={ globalStyles.viewBtn }>
              <Button color='maroon' title="Criar uma conta" onPress={() => this.props.navigation.navigate('UserFormComponent') } /> 
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

function mapStateToProps(state) {
    return { }
}

function mapDispatchToProps(dispatch) {
    return {
        login: () => dispatch({ type: 'LOGIN' }),
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(LoginComponent);
