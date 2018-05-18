import db from '../database';

export const status = (http, miflora) => {
  console.log('Adding route /status');
  http.get('/status', (req, res) => {
    const devices = miflora.devices.map(device => ({
      id: device.id,
      rssi: device.rssi,
      batteryLevel: device.batteryLevel,
      firmwareVersion: device.firmwareVersion,
      lastReading: device.lastReading,
    }));
    res.send({ devices });
  });
};

export const config = (http) => {
  console.log('Adding route /config');
  http.post('/config', ({ body }, res) => {
    const { callbackUrl } = body;
    if (!callbackUrl) {
      return res.sendStatus(400);
    }
    db.set('callbackUrl', callbackUrl)
      .write();
    console.log(`Set callback url to ${callbackUrl}`);
    return res.sendStatus(200);
  });
};
