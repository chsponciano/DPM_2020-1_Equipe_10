import * as React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import HomeComponent from './components/HomeComponent';
import TodoFormComponent from './components/TodoFormComponent';
import DoneTaskFormComponent from './components/DoneTaskFormComponent';

const Stack = createStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="Home" component={ HomeComponent } />
        <Stack.Screen name="Todo" component={ TodoFormComponent } />
        <Stack.Screen name="DoneTask" component={ DoneTaskFormComponent } />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
