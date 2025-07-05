import 'dart:async';

import 'package:battery_plus/battery_plus.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

void main() {
  CustomFlutterBinding();
  runApp(const MyApp());
}

class CustomFlutterBinding extends WidgetsFlutterBinding with BoostFlutterBinding {}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
   Map<String, FlutterBoostRouteFactory> routerMap = {
    '/': (RouteSettings settings, String? uniqueId) {
      return MaterialPageRoute(settings: settings, builder: (context) {
        return MyHomePage();
      });
    },
  };

  Route<dynamic>? routeFactory(RouteSettings settings, String? uniqueId) {
    FlutterBoostRouteFactory func = routerMap[settings.name] as FlutterBoostRouteFactory;
    return func(settings, uniqueId);
  }

  Widget appBuilder(Widget home) {
    return MaterialApp(
      home: home,
      builder: (_, __) {
        return home;
      },
    );
  }
  @override
  Widget build(BuildContext context) {
    return FlutterBoostApp(
      routeFactory,
      appBuilder: appBuilder,
    );
  }
}


class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});


  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  final Battery _battery = Battery();

  BatteryState? _batteryState;
  StreamSubscription<BatteryState>? _batteryStateSubscription;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  void initState() {
    super.initState();
    _battery.batteryState.then(_updateBatteryState);
    _batteryStateSubscription =
        _battery.onBatteryStateChanged.listen(_updateBatteryState);
  }

  void _updateBatteryState(BatteryState state) {
    if (_batteryState == state) return;
    setState(() {
      _batteryState = state;
    });
  }

  @override
  void dispose() {
    super.dispose();
    if (_batteryStateSubscription != null) {
      _batteryStateSubscription!.cancel();
    }
  }

  @override
  Widget build(BuildContext context) {
    
    // return Scaffold(
    //   appBar: AppBar(title: Text("ebkit")),
    //   body: Center(
    //     child: Column(
    //       mainAxisAlignment: MainAxisAlignment.center,
    //       children: <Widget>[
    //         const Text('You have pushed the button this many times:'),
    //         Text(
    //           '$_counter',
    //           style: Theme.of(context).textTheme.headlineMedium,
    //         ),
    //       ],
    //     ),
    //   ),
    //   floatingActionButton: FloatingActionButton(
    //     onPressed: _incrementCounter,
    //     tooltip: 'Increment',
    //     child: const Icon(Icons.add),
    //   ),
    // );


    return Scaffold(
      appBar: AppBar(
        title: const Text('Battery plus example app'),
        elevation: 4,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text(
              'Current battery state:',
              style: TextStyle(fontSize: 24),
            ),
            const SizedBox(height: 8),
            Text(
              '${_batteryState?.name}',
              style: const TextStyle(fontSize: 24),
            ),
            const SizedBox(height: 24),
            ElevatedButton(
              onPressed: () {
                _battery.batteryLevel.then(
                  (batteryLevel) {
                    if (context.mounted) {
                      showDialog<void>(
                        context: context,
                        builder: (_) => AlertDialog(
                          content: Text('Battery: $batteryLevel%'),
                          actions: <Widget>[
                            TextButton(
                              onPressed: () {
                                Navigator.pop(context);
                              },
                              child: const Text('OK'),
                            )
                          ],
                        ),
                      );
                    }
                  },
                );
              },
              child: const Text('Get battery level'),
            ),
            const SizedBox(height: 24),
            ElevatedButton(
              onPressed: () {
                _battery.isInBatterySaveMode.then(
                  (isInPowerSaveMode) {
                    if (context.mounted) {
                      showDialog<void>(
                        context: context,
                        builder: (_) => AlertDialog(
                          title: const Text(
                            'Is in Battery Save mode?',
                            style: TextStyle(fontSize: 20),
                          ),
                          content: Text(
                            "$isInPowerSaveMode",
                            style: const TextStyle(fontSize: 18),
                          ),
                          actions: <Widget>[
                            TextButton(
                              onPressed: () {
                                Navigator.pop(context);
                              },
                              child: const Text('Close'),
                            )
                          ],
                        ),
                      );
                    }
                  },
                );
              },
              child: const Text('Is in Battery Save mode?'),
            )
          ],
        ),
      ),
    );
  }
}
