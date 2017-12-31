import ip from 'ip';
import MiFlora from './miflora';
import SSDPServer from './ssdpServer';
import HTTP from './http';
import { status as addStatusRoute, config as addConfigRoute } from './http/routes';

const POLLING_INTERVAL = 30;

// Start http server
const http = new HTTP();
http.start();

// Start SSDP server
const ssdp = new SSDPServer();
ssdp.start(`http://${ip.address()}:${HTTP.LISTEN_PORT}/description.xml`)
  .then((advertisement) => {
    console.log('Started SSDP server');
    http.get('/description.xml', async (req, res) => {
      const details = await advertisement.service.details();
      res.send(details);
    });
  });

// Start MiFlora discovery
const miflora = new MiFlora();
miflora.scan(POLLING_INTERVAL);
miflora.on('reading', ({
  temperature, lux, moisture, fertility, time,
}, device) => {
  console.log('Device:', device.id);
  console.log('Time:', time.toUTCString());
  console.log('RSSI:', device.rssi);
  console.log('Battery level: %s %', device.batteryLevel);
  console.log('Firmware version: %s', device.firmwareVersion);
  console.log('Temperature: %s °C', temperature);
  console.log('Light: %s lux', lux);
  console.log('Moisture: %s %', moisture);
  console.log('Fertility: %s µS/cm', fertility);
});
miflora.on('error', (error) => {
  console.error(error);
});

// Add routes
addStatusRoute(http, miflora);
addConfigRoute(http);
