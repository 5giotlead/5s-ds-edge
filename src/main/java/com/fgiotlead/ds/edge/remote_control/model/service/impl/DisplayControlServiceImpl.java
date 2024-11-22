package com.fgiotlead.ds.edge.remote_control.model.service.impl;

import com.fazecast.jSerialComm.SerialPort;
import com.fgiotlead.ds.edge.remote_control.model.entity.DisplayCommandEntity;
import com.fgiotlead.ds.edge.remote_control.model.enumEntity.CommandType;
import com.fgiotlead.ds.edge.remote_control.model.service.DisplayControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class DisplayControlServiceImpl implements DisplayControlService {

    private SerialPort serialPort;

    @Override
    public ResponseEntity<Map<String, String>> remoteControl(CommandType commandType, int deviceId) {
        Map<CommandType, DisplayCommandEntity> commandMap = new HashMap<>();
        commandMap.put(
                CommandType.POWER_ON,
                DisplayCommandEntity.builder()
                        .length('8')
                        .cmdType('s')
                        .cmdCode(0x10)
                        .value1('0')
                        .value2('0')
                        .value3('1')
                        .build()
        );
        commandMap.put(
                CommandType.POWER_OFF,
                DisplayCommandEntity.builder()
                        .length('8')
                        .cmdType('s')
                        .cmdCode(0x10)
                        .value1('0')
                        .value2('0')
                        .value3('0')
                        .build()
        );
        commandMap.put(
                CommandType.POWER_STATUS,
                DisplayCommandEntity.builder()
                        .length('8')
                        .cmdType('g')
                        .cmdCode(0x10)
                        .value1('0')
                        .value2('0')
                        .value3('0')
                        .build()
        );
        if (this.serialPort == null) {
            this.initSerialPort();
        }
        return this.sendCommand(commandMap.get(commandType), deviceId);
    }

    private ResponseEntity<Map<String, String>> sendCommand(DisplayCommandEntity command, int deviceId) {
        Map<String, String> responseMap = new HashMap<>();
        HttpStatusCode statusCode;
        if (this.serialPort != null) {
            byte[] packet = new byte[9];
            packet[0] = (byte) command.getLength();
            packet[2] = (byte) deviceId;
            packet[3] = (byte) command.getCmdType();
            packet[4] = (byte) command.getCmdCode();
            packet[5] = (byte) command.getValue1();
            packet[6] = (byte) command.getValue2();
            packet[7] = (byte) command.getValue3();
            packet[8] = 0x0D;

            byte checksum = 0x00;
            for (int i = 0; i < packet.length; i++) {
                if (i != 1) {
                    checksum ^= packet[i];
                }
            }
            packet[1] = checksum;
            if (!this.serialPort.isOpen()) {
                this.serialPort.openPort();
            }
            this.serialPort.writeBytes(packet, packet.length);
            responseMap.put("message", "設定下達成功");
            statusCode = HttpStatus.OK;
        } else {
            responseMap.put("message", "設定下達失敗");
            statusCode = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(responseMap, statusCode);
    }

    private void initSerialPort() {
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length > 0) {
            this.serialPort = ports[0];
            this.serialPort.setBaudRate(9600);
            this.serialPort.setNumDataBits(8);
            this.serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
            this.serialPort.setParity(SerialPort.NO_PARITY);
            this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
            this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
        }
    }
}
