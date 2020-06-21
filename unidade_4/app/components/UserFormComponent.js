import { Formik } from 'formik';
import React, { Component } from 'react';
import { Alert, Button, StyleSheet, Text, TextInput, View } from 'react-native';
import { globalStyles } from '../styles/global.js';
import { doPost } from '../utils/HttpUtils';


export default class UserFormComponent extends Component {

  async save(values) {
    if (values.password !== values.confirmPassword) {
      Alert.alert('Erro de validação', 'As senhas são diferentes');
    } else {
      try {
        const response = await doPost('user', values);
        if (response.status === 201) {
          Alert.alert('Sucesso', 'Usuário cadastro com sucesso');
          this.props.navigation.goBack();
        } else {
          Alert.alert('Erro', JSON.stringify(response));
        }
      } catch (e) {
        Alert.alert('Erro', e.message);
      }
    }
    
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={ styles.titleView }>
          <Text style={ globalStyles.titleText }>{'DPM'}</Text>
        </View>
        <Formik
          style={styles.form}
          initialValues={{ name: '', username: '', email: '', password: '', confirmPassword: '' }}
          onSubmit={async (values) => this.save(values)}>
          {props => (
            <View>
              <TextInput
                style={globalStyles.input}
                placeholder='Nome completo'
                onChangeText={props.handleChange('name')}
                value={props.values.name}
              />

              <TextInput
                style={globalStyles.input}
                placeholder='E-mail'
                onChangeText={props.handleChange('email')}
                value={props.values.email}
              />

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

              <TextInput
                style={globalStyles.input}
                secureTextEntry={true}
                placeholder='Confirme sua senha'
                onChangeText={props.handleChange('confirmPassword')}
                value={props.values.confirmPassword}
              />

              <View style={ globalStyles.viewBtn }>
                <Button color='maroon' title="Criar conta" onPress={props.handleSubmit} /> 
              </View>

              <View style={ globalStyles.viewBtn }>
                <Button color='maroon' title="Voltar" onPress={() => this.props.navigation.goBack()} /> 
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