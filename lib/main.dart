import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(debugShowCheckedModeBanner: false, home: AuthGate());
  }
}

class AuthGate extends StatefulWidget {
  const AuthGate({super.key});
  @override
  State<AuthGate> createState() => _AuthGateState();
}

class _AuthGateState extends State<AuthGate> {
  static const platform = MethodChannel('native_auth');
  bool _authenticated = false;

  @override
  void initState() {
    super.initState();
    _authenticate();
  }

  Future<void> _authenticate() async {
    try {
      final bool result = await platform.invokeMethod('authenticate');
      setState(() {
        _authenticated = result;
      });
    } on PlatformException catch (e) {
      print('Error during authentication: $e');
      setState(() {
        _authenticated = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    if (!_authenticated) {
      return const Scaffold(body: Center(child: Text('Authenticating...')));
    }
    return const Scaffold(body: Center(child: Text('Welcome to the app!')));
  }
}