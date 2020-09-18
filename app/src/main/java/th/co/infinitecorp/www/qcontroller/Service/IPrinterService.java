package th.co.infinitecorp.www.qcontroller.Service;


import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QTicketInfo;
import th.co.infinitecorp.www.qcontroller.Uart.Uart;

public interface IPrinterService {
    PrinterService.OnDataReceivedListener getDataReceivedListener();
    public void PrintQTicketOnQPrint(Uart uart, byte ticketId, QTicketInfo info,PrinterService printerService);

}
