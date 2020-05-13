import * as React from 'react';
import { Alert, AsyncStorage, Button, StyleSheet, View } from 'react-native';
import t from 'tcomb-form-native';

export default function TodoFormComponent({ navigation }: any) {

    const Todo = t.struct({
        name: t.String,
        desc: t.String,
        obs: t.String,
    });
    const options = {
        fields: {
            name: {
                label: 'Nome'
            },
            desc: {
                label: 'Descrição'
            },
            obs: {
                label: 'Observação'
            },
        }
    }

    const Form = t.form.Form;
    let formData: any;

    const onPressSubmitForm = async () => {

        try {
            const curr = await AsyncStorage.getItem('TodoData')
            if (!curr) {
                try {
                    await AsyncStorage.setItem('TodoData', JSON.stringify(formData.getValue()))
                    navigation.goBack()
                } catch (error) {
                    console.error(error);
                    Alert.alert(
                        "Erro",
                        "Ocorreu um erro inesperado ao salvar os dados localmente",
                        [ { text: "OK" } ]
                    )
                }
            } else {
                Alert.alert(
                    "Erro",
                    "Você deve antes sincronizar a tarefa ja cadastrada com o web service",
                    [ { text: "OK" } ]
                )
            }
        } catch(error) {
            console.error(error);
            Alert.alert(
                "Erro",
                "Ocorreu um erro inesperado ao buscar os dados salvos localmente",
                [ { text: "OK" } ]
            )
        }
        

    }

    return (
        <View style={ styles.container }>
            <Form type={Todo} ref={(c: any) => formData = c} options={ options } />
            <View style={ styles.btn }>
                <Button
                    onPress={ onPressSubmitForm }
                    title="Salvar tarefa localmente"
                    color="#841584"
                    accessibilityLabel="Salvar tarefa localmente"
                />
            </View>
            <View style={ styles.btn }>
                <Button
                    onPress={ () => navigation.goBack() }
                    title="Cancelar"
                    color="#841584"
                    accessibilityLabel="Cancelar"
                />
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
      justifyContent: 'center',
      marginTop: 50,
      padding: 20,
      backgroundColor: '#ffffff',
    },
    btn: {
        marginTop: 5
    }
  });
