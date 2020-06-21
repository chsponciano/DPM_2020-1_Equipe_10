import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import DeviceFormComponent from './DeviceFormComponent';
import LoadingComponent from './LoadingComponent';
import LoginComponent from './LoginComponent';
import MainComponent from './MainComponent';
import UserFormComponent from './UserFormComponent';



const Stack = createStackNavigator();

export class RouterApp extends Component {

  constructor(props) {
    super(props);
    this.state = {
      loading: false,
    };
  }

  render() {
    return this.state.loading ? this.renderLoading : !this.props.loggedIn ? this.renderMainStack() : this.renderLoginStack();
  }

  renderLoading() {
    return (
      <LoadingComponent/>
    )
  }

  renderMainStack() {
    return (
      <NavigationContainer>
        <Stack.Navigator screenOptions={{ headerShown: false }}>
          <Stack.Screen name="MainComponent" component={ MainComponent } />
          <Stack.Screen name="DeviceFormComponent" component={ DeviceFormComponent } />
        </Stack.Navigator>
      </NavigationContainer>
    );
  }

  renderLoginStack() {
    return (
      <NavigationContainer>
        <Stack.Navigator screenOptions={{ headerShown: false }}>
          <Stack.Screen name="LoginComponent" component={ LoginComponent } />
          <Stack.Screen name="UserFormComponent" component={ UserFormComponent } />
        </Stack.Navigator>
      </NavigationContainer>
    )
  }

}

function mapStateToProps(state) {
    return { 
      loggedIn: state.loggedIn
    }
}

function mapDispatchToProps(dispatch) {
    return { }
}

export default connect(mapStateToProps, mapDispatchToProps)(RouterApp);
