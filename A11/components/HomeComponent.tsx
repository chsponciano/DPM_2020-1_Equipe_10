import * as React from 'react';
import { Button, StyleSheet, View, Alert, AsyncStorage } from 'react-native';

export default function HomeComponent({ navigation }: any) {

    const syncTodo = async () => {
        try {
            const todoLocal = await AsyncStorage.getItem('TodoData')
            const doneTaskLocal = await AsyncStorage.getItem('DoneTaskData')
            if (!todoLocal && !doneTaskLocal) {
                Alert.alert(
                    "Erro",
                    "Não existem dados pendentes para serem sincronizados",
                    [ { text: "OK" } ]
                )
                return;
            } 
            
            if (todoLocal) {
                const response = await fetch('https://dpma11.firebaseio.com/todo/NZVpvzG1jB9dXEG35IN7/documents/todo.json', {
                    method: 'POST',
                    body: todoLocal
                })
                if (response.status < 200 || response.status > 299) {
                    Alert.alert(
                        "Erro",
                        "Ocorreu um erro ao sincronizar os dados",
                        [ { text: "OK" } ]
                    )
                } else {
                    await AsyncStorage.removeItem('TodoData');
                    Alert.alert(
                        "Sucesso",
                        "Operação realizada com sucesso"
                    )
                }
            }

            if (doneTaskLocal) {
                const response = await fetch('https://dpma11.firebaseio.com/todo/NZVpvzG1jB9dXEG35IN7/documents/done_task.json', {
                    method: 'POST',
                    body: doneTaskLocal
                })
                if (response.status < 200 || response.status > 299) {
                    Alert.alert(
                        "Erro",
                        "Ocorreu um erro ao sincronizar os dados",
                        [ { text: "OK" } ]
                    )
                } else {
                    await AsyncStorage.removeItem('DoneTaskData');
                    Alert.alert(
                        "Sucesso",
                        "Operação realizada com sucesso"
                    )
                }
            }
        
        } catch(error) {
            console.error(error)
            Alert.alert(
                "Erro",
                "Ocorreu um erro ao sincronizar os dados",
                [ { text: "OK" } ]
            )
        }
    }

    return (
        <View style={ styles.container }>
            <View style={ styles.btn }>
                <Button
                    onPress={ () => navigation.navigate('Todo') }
                    title="Cadastrar tarefa"
                    color="#841584"
                    accessibilityLabel="Cadastrar tarefa"
                />
            </View>
            <View style={ styles.btn }>
                <Button
                    onPress={ () => navigation.navigate('DoneTask') }
                    title="Cadastrar tarefa concluida"
                    color="#841584"
                    accessibilityLabel="Cadastrar tarefa concluida"
                />
            </View>
            <View style={ styles.btn }>
                <Button
                    onPress={ () => syncTodo() }
                    title="Sincronizar tarefas"
                    color="#841584"
                    accessibilityLabel="Vizualizar tarefas"
                />
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: '#fff',
      alignItems: 'center',
      justifyContent: 'center',
    },
    btn: {
        marginTop: 5
    }
});
  
