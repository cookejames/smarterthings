import EventEmitter from 'events';
import { promisify } from 'util';
import Reading from './reading';

const DATA_SERVICE_UUID = '0000120400001000800000805f9b34fb';
const DATA_CHARACTERISTIC_UUID = '00001a0100001000800000805f9b34fb';
const FIRMWARE_CHARACTERISTIC_UUID = '00001a0200001000800000805f9b34fb';
const REALTIME_CHARACTERISTIC_UUID = '00001a0000001000800000805f9b34fb';
const REALTIME_META_VALUE = Buffer.from([0xA0, 0x1F]);
const SERVICE_UUIDS = [DATA_SERVICE_UUID];
const CHARACTERISTIC_UUIDS = [DATA_CHARACTERISTIC_UUID, FIRMWARE_CHARACTERISTIC_UUID, REALTIME_CHARACTERISTIC_UUID];

export default class MiFloraDevice extends EventEmitter {
  constructor(peripheral, period) {
    super();
    this.peripheral = peripheral;
    this.period = period;
    peripheral.on('connect', async () => {
      try {
        console.log(`Device connected - ${this.id}`);
        const { data, firmware, realtime } = await this.getCharacteristics();
        await MiFloraDevice.writeCharacteristic(realtime, REALTIME_META_VALUE);
        const { firmwareVersion, batteryLevel } =
          MiFloraDevice.decodeBatteryAndFirmware(await MiFloraDevice.readCharacteristic(firmware));
        const reading = MiFloraDevice.decodeData(await MiFloraDevice.readCharacteristic(data));
        this.firmwareVersion = firmwareVersion;
        this.batteryLevel = batteryLevel;
        this.rssi = peripheral.rssi;
        this.lastReading = reading;
        this.emit('data', reading);
        peripheral.disconnect();
      } catch (error) {
        console.error(error);
        this.emit('error', error);
      }
    });
  }

  connect() {
    this.peripheral.connect();
    this.interval = setInterval(() => this.peripheral.connect(), this.period * 1000);
  }

  disconnect() {
    this.peripheral.disconnect();
    clearInterval(this.interval);
    this.interval = undefined;
  }

  get id() {
    return this.peripheral.id;
  }

  async getCharacteristics() {
    this.peripheral.discoverSomeServicesAndCharacteristicsPromised =
      promisify(this.peripheral.discoverSomeServicesAndCharacteristics);
    const [service] =
      await this.peripheral.discoverSomeServicesAndCharacteristicsPromised(SERVICE_UUIDS, CHARACTERISTIC_UUIDS);
    const { characteristics } = service;
    return {
      data: characteristics.filter(characteristic => characteristic.uuid === DATA_CHARACTERISTIC_UUID)[0],
      realtime: characteristics.filter(characteristic => characteristic.uuid === REALTIME_CHARACTERISTIC_UUID)[0],
      firmware: characteristics.filter(characteristic => characteristic.uuid === FIRMWARE_CHARACTERISTIC_UUID)[0],
    };
  }

  static readCharacteristic(characteristic) {
    characteristic.readPromisified = promisify(characteristic.read);
    return characteristic.readPromisified();
  }

  static writeCharacteristic(characteristic, value) {
    characteristic.writePromisified = promisify(characteristic.write);
    return characteristic.writePromisified(value, false);
  }

  static decodeData(data) {
    return new Reading(
      data.readUInt16LE(0) / 10, // temperature
      data.readUInt32LE(3), // lux
      data.readUInt16BE(6), // moisture
      data.readUInt16LE(8), // fertility
      new Date()
    );
  }

  static decodeBatteryAndFirmware(data) {
    const batteryLevel = parseInt(data.toString('hex', 0, 1), 16);
    const firmwareVersion = data.toString('ascii', 2, data.length);
    return { batteryLevel, firmwareVersion };
  }
}
