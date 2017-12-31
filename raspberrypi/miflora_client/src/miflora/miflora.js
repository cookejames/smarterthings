import noble from 'noble';
import EventEmitter from 'events';
import MiFloraDevice from './device';

const SCAN_PERIOD_DEFAULT = 1800;

export default class MiFlora extends EventEmitter {
  constructor() {
    super();
    this.devices = [];
  }

  scan(period = SCAN_PERIOD_DEFAULT) {
    this.period = period;

    noble.on('stateChange', (state) => {
      if (state === 'poweredOn') {
        console.log('Started bluetooth scanning');
        noble.startScanning();
      } else {
        console.warn('Stopped bluetooth scanning');
        noble.stopScanning();
      }
    });

    noble.on('discover', (peripheral) => {
      const { id, advertisement: { localName: name } } = peripheral;
      console.log('Discovered:', id, name);
      if (name !== 'Flower care') return;

      const matches = this.devices.filter(device => device.id === peripheral.id);
      if (matches.length === 0) {
        console.log('Adding new device');
        const device = new MiFloraDevice(peripheral, this.period);
        device.connect();
        this.devices.push(device);
        device.on('data', data => this.emit('reading', data, device));
      } else {
        console.log('Device already exists');
      }
    });
  }

  stop() {
    this.devices.forEach(device => device.disconnect());
  }
}
