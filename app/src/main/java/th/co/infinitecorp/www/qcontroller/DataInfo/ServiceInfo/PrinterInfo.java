package th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;

public class PrinterInfo {
    public class TicketInfo{

        byte  ticketId;
        byte copies;
        byte type;
        byte lang;
        QInfo queue;
        short waitQ;
        byte date;
        byte month;
        byte year;
        short curTime;
        short aprxServeTime;
        short numPrint;

        public byte getTicketId() {
            return ticketId;
        }

        public void setTicketId(byte ticketId) {
            this.ticketId = ticketId;
        }

        public byte getCopies() {
            return copies;
        }

        public void setCopies(byte copies) {
            this.copies = copies;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public byte getLang() {
            return lang;
        }

        public void setLang(byte lang) {
            this.lang = lang;
        }

        public QInfo getQueue() {
            return queue;
        }

        public void setQueue(QInfo queue) {
            this.queue = queue;
        }

        public short getWaitQ() {
            return waitQ;
        }

        public void setWaitQ(short waitQ) {
            this.waitQ = waitQ;
        }

        public byte getDate() {
            return date;
        }

        public void setDate(byte date) {
            this.date = date;
        }

        public byte getMonth() {
            return month;
        }

        public void setMonth(byte month) {
            this.month = month;
        }

        public byte getYear() {
            return year;
        }

        public void setYear(byte year) {
            this.year = year;
        }

        public short getCurTime() {
            return curTime;
        }

        public void setCurTime(short curTime) {
            this.curTime = curTime;
        }

        public short getAprxServeTime() {
            return aprxServeTime;
        }

        public void setAprxServeTime(short aprxServeTime) {
            this.aprxServeTime = aprxServeTime;
        }

        public short getNumPrint() {
            return numPrint;
        }

        public void setNumPrint(short numPrint) {
            this.numPrint = numPrint;
        }
    }
}
