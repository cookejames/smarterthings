import request from 'request-promise-native';
import MiFlora from './miflora';
import SSDPServer from './ssdpServer';
import HTTP from './http';
import { status as addStatusRoute, config as addConfigRoute } from './http/routes';
import db from './database';

const POLLING_INTERVAL = 300;

// Start http server
const http = new HTTP();
http.start();

// Start SSDP server
const ssdp = new SSDPServer();
ssdp.start(`http://${HTTP.ADDRESS}:${HTTP.PORT}/description.xml`)
  .then((advertisement) => {
    console.log('Started SSDP server');
    http.get('/description.xml', async (req, res) => {
      const details = await advertisement.service.details();
      res.set('Content-Type', 'text/xml');
      res.send(details);
    });
  });

// Start MiFlora discovery
const miflora = new MiFlora();
miflora.scan(POLLING_INTERVAL);
miflora.on('reading', async ({
  temperature, lux, moisture, fertility, time,
}, device) => {
  const callbackUrl = db.get('callbackUrl').value();
  if (!callbackUrl) {
    console.error('Cannot send reading the callback url has not been set.');
  } else {
    console.log('Device:', device.id);
    console.log('Time:', time.toUTCString());
    console.log('RSSI:', device.rssi);
    console.log('Battery level: %s %', device.batteryLevel);
    console.log('Firmware version: %s', device.firmwareVersion);
    console.log('Temperature: %s °C', temperature);
    console.log('Light: %s lux', lux);
    console.log('Moisture: %s %', moisture);
    console.log('Fertility: %s µS/cm', fertility);
    const options = {
      method: 'POST',
      uri: callbackUrl,
      body: {
        deviceId: device.id,
        time: time.toUTCString(),
        rssi: device.rssi,
        batteryLevel: device.batteryLevel,
        firmwareVersion: device.firmwareVersion,
        temperature,
        lux,
        moisture,
        fertility,
      },
      json: true,
    };
    await request(options).catch(error => console.error('Error sending reading:', error));
    console.log(`Reading sent to ${callbackUrl}`);
  }
});
miflora.on('error', (error) => {
  console.error(error);
});

// Add routes
addStatusRoute(http, miflora);
addConfigRoute(http);
