package th.co.infinitecorp.www.qcontroller.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QTicketInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.Uart.Uart;
import th.co.infinitecorp.www.qcontroller.Utils.Convert;
import th.co.infinitecorp.www.qcontroller.Utils.Protocol;


public class PrinterService implements IPrinterService {

    /*Interface*/
    private OnDataReceivedListener dataReceivedListener;
    public void setOnDataReceivedListener(OnDataReceivedListener listener) {
        this.dataReceivedListener = listener;
    }

    @Override
    public OnDataReceivedListener getDataReceivedListener() {
        return null;
    }

    public interface OnDataReceivedListener {
        void onDataReceived(boolean status);
        void onReceiveFail();
    }
    /*Command To QPrint*/
    public  void CommunicatePrinter(Uart uart,PrinterService printerService) {
        boolean resp=scanQPrint(uart,printerService);

    }

    /*Sync*/
    private  boolean scanQPrint(Uart uart,final PrinterService printerService) {
        boolean resp=false;
        byte[] bytes=prepareDataSync();
        EventBus.getDefault().post(new DebugMessageEvent("scanQPrint"));
        uart.SendReceive_Byte(500,bytes,true);
        uart.setOnDataReceivedListener(new Uart.OnDataReceivedListener() {
            @Override
            public boolean onDataReceived(byte[] bytes) {

                String str=Convert.ByteArrayToHexStringWithSpace(bytes);
                EventBus.getDefault().post(new DebugMessageEvent("Serial Rev Len="+bytes.length+" Hex="+str));
                /*
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        printerService.getDataReceivedListener().onDataReceived(true);
                    }
                });
                */
                byte frameStatus=Protocol.FRAME_STATUS.CORRECT;
                frameStatus=Protocol.Verifed_DataFrame(bytes);
                if(frameStatus==Protocol.FRAME_STATUS.CORRECT) {
                    EventBus.getDefault().post(new DebugMessageEvent("Serial Rev OK***"));
                }else {
                    EventBus.getDefault().post(new DebugMessageEvent("Serial Rev Status="+frameStatus));
                }
                //Handle Event
                if(frameStatus==Protocol.FRAME_STATUS.CORRECT) {
                    byte cmd=0x00;
                    switch (cmd){
                        case 0x00: break;
                        case 0x01: break;
                    }
                }
                return true;
            }
            @Override
            public boolean onReceiveFail(byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("Serial Rev Fail!"));
                /*
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        printerService.getDataReceivedListener().onDataReceived(false);
                    }
                });
                */
                return false;
            }
        });

        return false;
    }
    private static   byte[] prepareDataSync() {
        byte[] bytes=new byte[1024*2];
        int idx=0;

        byte[] b = Arrays.copyOf(bytes, idx);
        return Protocol.prepareData_Protocol_V1(Protocol.QPRINT_CMD.SYNC,Protocol.Uart_Address.Printer1,b);
    }

    /*PrintQTicket*/
    public void PrintQTicketOnQPrint(Uart uart, byte ticketId, QTicketInfo info,PrinterService printerService){
        printQTicketOnQPrint(uart,ticketId,info,printerService);
    }
    private  boolean printQTicketOnQPrint(final Uart uart,final byte ticketId,final QTicketInfo info,final PrinterService printerService) {

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes= preparePrintQTicket(ticketId,info);
                String str=Convert.ByteArrayToHexStringWithSpace(bytes);
                EventBus.getDefault().post(new DebugMessageEvent("PrintQ_Serial Send Hex:"+str));
                uart.SendReceive_Byte(500,bytes,true);
                uart.setOnDataReceivedListener(new Uart.OnDataReceivedListener() {
                    @Override
                    public boolean onDataReceived(byte[] bytes) {

                        String str=Convert.ByteArrayToHexStringWithSpace(bytes);
                        EventBus.getDefault().post(new DebugMessageEvent("PrintQ_Serial Rev Len="+bytes.length+" Hex="+str));
                        byte resp=Protocol.Verifed_DataFrame(bytes);
                        if(resp==Protocol.FRAME_STATUS.CORRECT) {
                            byte cmd=bytes[4];
                            if(cmd==Protocol.QPRINT_CMD.PRINT_QTICKET)
                                EventBus.getDefault().post(new DebugMessageEvent("***PrintQ_Serial Rev OK***"));
                            else
                                EventBus.getDefault().post(new DebugMessageEvent("***PrintQ_Serial Rev CMD Fail***"));
                        }else {
                            EventBus.getDefault().post(new DebugMessageEvent("***PrintQ_Serial Rev Fail="+resp));
                        }
                        /*
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                printerService.getDataReceivedListener().onDataReceived(true);
                            }
                        });
                        */
                        return true;
                    }
                    @Override
                    public boolean onReceiveFail(byte[] bytes) {
                        EventBus.getDefault().post(new DebugMessageEvent("***PrintQ_Serial Rev TimeOut***"));
                        /*
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                printerService.getDataReceivedListener().onDataReceived(false);
                            }
                        });
                        */
                        return false;
                    }
                });
            }
        });
        thread.start();
        return false;
    }
    private  boolean printQTicketOnQPrint2(final Uart uart,final byte ticketId,final QTicketInfo info,final PrinterService printerService) {
       while (true) {
           byte[] bytes = preparePrintQTicket(ticketId, info);
           String str = Convert.ByteArrayToHexStringWithSpace(bytes);
           EventBus.getDefault().post(new DebugMessageEvent("PrintQ_Serial Send Hex:" + str));
           uart.SendReceive_Byte(500, bytes, true);
           uart.setOnDataReceivedListener(new Uart.OnDataReceivedListener() {
               @Override
               public boolean onDataReceived(byte[] bytes) {

                   String str = Convert.ByteArrayToHexStringWithSpace(bytes);
                   EventBus.getDefault().post(new DebugMessageEvent("PrintQ_Serial Rev Len=" + bytes.length + " Hex=" + str));
                   byte resp = Protocol.Verifed_DataFrame(bytes);
                   if (resp == Protocol.FRAME_STATUS.CORRECT) {
                       byte cmd = bytes[4];
                       if (cmd == Protocol.QPRINT_CMD.PRINT_QTICKET)
                           EventBus.getDefault().post(new DebugMessageEvent("***PrintQ_Serial Rev OK***"));
                       else
                           EventBus.getDefault().post(new DebugMessageEvent("***PrintQ_Serial Rev CMD Fail***"));
                   } else {
                       EventBus.getDefault().post(new DebugMessageEvent("***PrintQ_Serial Rev Fail=" + resp));
                   }
                        /*
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                printerService.getDataReceivedListener().onDataReceived(true);
                            }
                        });
                        */
                   return true;
               }

               @Override
               public boolean onReceiveFail(byte[] bytes) {
                   EventBus.getDefault().post(new DebugMessageEvent("***PrintQ_Serial Rev TimeOut***"));
                        /*
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                printerService.getDataReceivedListener().onDataReceived(false);
                            }
                        });
                        */
                   return false;
               }
           });
           break;
       }

        return false;
    }
    private static   byte[] preparePrintQTicket(byte ticketId, QTicketInfo info) {
        byte[] bytes=new byte[1024*2];
        int idx=0;
        /*
        STX-ADDR-LEN[2]-CMD-ticketId-copies-type-lang-Qno[4]-waitQ2bytes
                -Date-Month-Year-hh-mm-ss-aprxServeTime2bytes-numPrint2bytes
                -Prt1_type-Prt1_len(2)-Prt1_Data
                */
        //Testing Fix Data
        bytes[idx++]=ticketId; //Ticket ID
        bytes[idx++]=info.getCopy();

        bytes[idx++]=0;  //None
        bytes[idx++]=0;  //None

        bytes[idx++]=info.getqType();
        bytes[idx++]=info.getqAlp();
        bytes[idx++]=Convert.GetByteHigh(info.getqNum());
        bytes[idx++]=Convert.GetByteLow(info.getqNum());

        bytes[idx++]=Convert.GetByteHigh(info.getWaitQ());
        bytes[idx++]=Convert.GetByteLow(info.getWaitQ());

        bytes[idx++]=info.getDate();
        bytes[idx++]=info.getMonth();
        bytes[idx++]=info.getYear();
        bytes[idx++]=info.getHour();
        bytes[idx++]=info.getMinute();
        bytes[idx++]=info.getSecond();

        bytes[idx++]=Convert.GetByteHigh(info.getWaitTime());
        bytes[idx++]=Convert.GetByteLow(info.getWaitTime());

        bytes[idx++]=Convert.GetByteHigh(info.getNumPrint());
        bytes[idx++]=Convert.GetByteLow(info.getNumPrint());

        bytes[idx++]=0x00;
        bytes[idx++]=0x00;

        byte[] b = Arrays.copyOf(bytes, idx);
        return Protocol.prepareData_Protocol_V1(Protocol.QPRINT_CMD.PRINT_QTICKET,Protocol.Uart_Address.Printer1,b);
    }

    /*Print Maintenance*/
    private static boolean printTicket_MaintenanceReportOnQPrint(Uart uart) {
        byte[] bytes= preparePrintTicket_Maintenance_Report();
        String str=Convert.ByteArrayToHexStringWithSpace(bytes);
        EventBus.getDefault().post(new DebugMessageEvent("Serial Send Hex:"+str));
        uart.SendReceive_Byte(500,bytes,true);
        uart.setOnDataReceivedListener(new Uart.OnDataReceivedListener() {
            @Override
            public boolean onDataReceived(byte[] bytes) {

                String str=Convert.ByteArrayToHexStringWithSpace(bytes);
                EventBus.getDefault().post(new DebugMessageEvent("Serial Rev Len="+bytes.length+" Hex="+str));
                return true;

            }
            @Override
            public boolean onReceiveFail(byte[] bytes) {
                EventBus.getDefault().post(new DebugMessageEvent("Serial Rev Fail!"));
                return false;
            }
        });
        return false;
    }
    public static void PrintTicket_MaintenanceReportOnQPrint(Uart uart){
        printTicket_MaintenanceReportOnQPrint(uart);
    }
    private static   byte[] preparePrintTicket_Maintenance_Report() {
        //stx-addr-bh-bl-MAINTENANCE-copies-Date-Month-Year-Hour-Min-Sec-PrinterID-cutTimes[4]-sum-eot
        byte[] bytes=new byte[1024*2];
        int idx=0;
        //Testing Fix Data
        bytes[idx++]=1;

        bytes[idx++]=0x01;
        bytes[idx++]=0x02;
        bytes[idx++]=0x03;
        bytes[idx++]=0x04;
        bytes[idx++]=0x05;
        bytes[idx++]=0x06;

        bytes[idx++]=0x01;

        bytes[idx++]=0x00;
        bytes[idx++]=0x00;
        bytes[idx++]=0x00;
        bytes[idx++]=0x01;

        byte[] b = Arrays.copyOf(bytes, idx);
        return Protocol.prepareData_Protocol_V1(Protocol.QPRINT_CMD.PRINT_MAINTENANCE_REPORT,Protocol.Uart_Address.Printer1,b);
    }
    private static byte[] prepareDataForSend(byte cmd,byte deviceId,byte[] bytes) {
        byte[] sBytes= Protocol.prepareData_Protocol_V1(cmd,deviceId,bytes);
        return sBytes;
    }
}
