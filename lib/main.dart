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
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _checkSecurityAndAuthenticate();
  }

  Future<void> _checkSecurityAndAuthenticate() async {
    try {
      // First, check if device has any security locks
      final bool hasDeviceSecurity = await platform.invokeMethod(
        'hasDeviceSecurity',
      );

      if (!hasDeviceSecurity) {
        // No security locks set - skip authentication
        setState(() {
          _authenticated = true;
          _isLoading = false;
        });
        return;
      }

      // Device has security locks - proceed with authentication
      final bool result = await platform.invokeMethod('authenticate');
      setState(() {
        _authenticated = result;
        _isLoading = false;
      });
    } on PlatformException catch (e) {
      print('Error during security check or authentication: $e');
      setState(() {
        _authenticated = false;
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_isLoading) {
      return const Scaffold(
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              CircularProgressIndicator(),
              SizedBox(height: 16),
              Text('Checking device security...'),
            ],
          ),
        ),
      );
    }

    if (!_authenticated) {
      return const Scaffold(
        body: Center(child: Text('Authentication failed or cancelled')),
      );
    }

    return const Scaffold(body: Center(child: Text('Welcome to the app!')));
  }
}
