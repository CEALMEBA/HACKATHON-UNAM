package com.kubeet.smarturban


import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.hardware.usb.*
import android.net.Uri
import android.os.*
import android.os.health.SystemHealthManager
import android.print.*
import android.print.pdf.PrintedPdfDocument
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.common.api.internal.RegisterListenerMethod
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.hussein.startup.R
import com.kubeet.models.TicketPrices_Document
import com.kubeet.models.Wallets_Document
import com.kubeet.models.sessionStorage
import kotlinx.android.synthetic.main.fragment_cupones.view.*
import kotlinx.android.synthetic.main.fragment_print.*
import kotlinx.android.synthetic.main.fragment_ticket.*
import kotlinx.android.synthetic.main.fragment_ticket.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.FileSystems
import java.text.SimpleDateFormat
import java.util.*


class PrintFragment : Fragment() {

    companion object {
        fun newInstance(): PrintFragment = PrintFragment()
    }

    lateinit var mUsbManager: UsbManager
    private var mDevice: UsbDevice? = null
    private var mConnection: UsbDeviceConnection? = null
    private var mInterface: UsbInterface? = null
    private var mEndPoint: UsbEndpoint? = null
    private var mPermissionIntent: PendingIntent? = null
    lateinit var ed_txt:EditText
    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    val forceCLaim = true
    var mDeviceList: HashMap<String, UsbDevice>? = null
    var mDeviceIterator: Iterator<UsbDevice>? = null
    var testBytes: ByteArray? = null
    lateinit var device:UsbDevice

    private val MILS_PER_INCH = 1000

    lateinit var  mlistView : ListView
    //var  ListTickets = ArrayList<TicketPrices_Document>()
    //var scannedResult: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        container!!.removeAllViews()
        val view = inflater!!.inflate(R.layout.fragment_print,container,false)

        //mlistView = view.findViewById<ListView>(R.id.listTicket) as ListView

        //callTickets()





        ed_txt = view.findViewById(R.id.ed_txt) as EditText
        val print = view.findViewById(R.id.btnPrint) as Button
        //mUsbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        mUsbManager = context!!.getSystemService(Context.USB_SERVICE) as UsbManager
        mDeviceList = mUsbManager!!.getDeviceList()


        print.setOnClickListener{
            Print(btnPrint)
        }



/*
        USB@  var deviceList = mUsbManager.getDeviceList()
        println("Lista de dispositivos:" + deviceList)

        deviceList = mUsbManager.getDeviceList()
        device = deviceList.get("")!!
*/

        /*if ((mDeviceList as HashMap<String, UsbDevice>?)!!.size > 0)
        {
            mDeviceIterator = (mDeviceList as HashMap<String, UsbDevice>?)!!.values.iterator()
            Toast.makeText(context, "Device List Size: " + (mDeviceList as HashMap<String, UsbDevice>?)!!.size, Toast.LENGTH_SHORT).show()
            val textView = view.findViewById(R.id.usbDevice) as TextView
            var usbDevice = ""
            while ((mDeviceIterator as MutableIterator<UsbDevice>).hasNext())
            {
                val usbDevice1 = (mDeviceIterator as MutableIterator<UsbDevice>).next()
                usbDevice += ("\n" +
                        "DeviceID: " + usbDevice1.getDeviceId() + "\n" +
                        "DeviceName: " + usbDevice1.getDeviceName() + "\n" +
                        "Protocol: " + usbDevice1.getDeviceProtocol() + "\n" +
                        "Product Name: " + usbDevice1.getProductName() + "\n" +
                        "Manufacturer Name: " + usbDevice1.getManufacturerName() + "\n" +
                        "DeviceClass: " + usbDevice1.getDeviceClass() + " - " + translateDeviceClass(usbDevice1.getDeviceClass()) + "\n" +
                        "DeviceSubClass: " + usbDevice1.getDeviceSubclass() + "\n" +
                        "VendorID: " + usbDevice1.getVendorId() + "\n" +
                        "ProductID: " + usbDevice1.getProductId() + "\n")
                val interfaceCount = usbDevice1.getInterfaceCount()
                Toast.makeText(context, "INTERFACE COUNT: " + (interfaceCount).toString(), Toast.LENGTH_SHORT).show()
                mDevice = usbDevice1
                Toast.makeText(context, "Device is attached", Toast.LENGTH_SHORT).show()
                textView.setText(usbDevice)
            }
            mPermissionIntent = PendingIntent.getBroadcast(context, 0, Intent(ACTION_USB_PERMISSION), 0)
            //val filter = IntentFilter(ACTION_USB_PERMISSION)
            //registerReceiver(mUsbReceiver, filter)

            LocalBroadcastManager.getInstance(context!!)
                    .registerReceiver(mUsbReceiver, IntentFilter(ACTION_USB_PERMISSION))
            mUsbManager!!.requestPermission(mDevice, mPermissionIntent)
        }
        else
        {
            Toast.makeText(context, "Please conection  printer via USB", Toast.LENGTH_SHORT).show()
        }
        try {
        print.setOnClickListener(
                object: View.OnClickListener {
                    override fun onClick(view:View) {
                        print(mConnection!!, mInterface!!)
                    }
                })



        }catch (ex:Exception){
            println("errorPrintTicket $ex")
        }*/

        return view
    }


    fun Print(button: Button) {
        /**
         * It is better to make this class universal.
         * delegete computePageCount() & drawPage() into class Render.
         */
        class DemoDocumentAdapter(private val mParentActivity: Activity) : PrintDocumentAdapter() {
            private var currentAttributes: PrintAttributes? = null

            private var totalPages: Int = 0
            private var mRenderPageWidth: Int = 0
            private var mRenderPageHeight: Int = 0
            var mIsPortrait = false
            private var printDocumentInfo: PrintDocumentInfo? = null

            // margin
            private var margin_left = 0
            private var margin_right = 0
            private var margin_top = 0
            private var margin_bottom = 0
            fun setCurrentAttributes(currentAttributes: PrintAttributes) {
                this.currentAttributes = currentAttributes
                val density_w = currentAttributes.resolution!!.horizontalDpi
                val density_h = currentAttributes.resolution!!.verticalDpi

                margin_left = Math.max(margin_left, (density_w * currentAttributes.minMargins!!.leftMils.toFloat() / MILS_PER_INCH).toInt())
                margin_top = Math.max(margin_top, (density_h * currentAttributes.minMargins!!.topMils.toFloat() / MILS_PER_INCH).toInt())
                margin_right = Math.max(margin_right, (density_w * currentAttributes.minMargins!!.rightMils.toFloat() / MILS_PER_INCH).toInt())
                margin_bottom = Math.max(margin_bottom, (density_h * currentAttributes.minMargins!!.bottomMils.toFloat() / MILS_PER_INCH).toInt())

            }

            override fun onLayout(oldAttributes: PrintAttributes, newAttributes: PrintAttributes, cancellationSignal: CancellationSignal, callback: PrintDocumentAdapter.LayoutResultCallback, extras: Bundle) {
                Log.d("TEST", "onLayout s")
                currentAttributes = newAttributes

                // Respond to cancellation request
                if (cancellationSignal.isCanceled) {
                    callback.onLayoutCancelled()
                    return
                }

                var shouldLayout = false // redraw need only on changes page width, height or orientation

                val density_w = currentAttributes!!.resolution!!.horizontalDpi
                val density_h = currentAttributes!!.resolution!!.verticalDpi

                margin_left = Math.max(margin_left, (density_w * currentAttributes!!.minMargins!!.leftMils.toFloat() / MILS_PER_INCH).toInt())
                Log.d("TEST", "margin_left " + margin_left.toString())

                margin_right = Math.max(margin_right, (density_w * currentAttributes!!.minMargins!!.rightMils.toFloat() / MILS_PER_INCH).toInt())
                val contentWidth = (density_w * currentAttributes!!.mediaSize!!
                        .widthMils.toFloat() / MILS_PER_INCH).toInt() - margin_left - margin_right
                if (mRenderPageWidth != contentWidth) {
                    mRenderPageWidth = contentWidth
                    shouldLayout = true
                }

                margin_top = Math.max(margin_top, (density_h * currentAttributes!!.minMargins!!.topMils.toFloat() / MILS_PER_INCH).toInt())
                margin_bottom = Math.max(margin_bottom, (density_h * currentAttributes!!.minMargins!!.bottomMils.toFloat() / MILS_PER_INCH).toInt())

                val contentHeight = (density_h * currentAttributes!!.mediaSize!!
                        .heightMils.toFloat() / MILS_PER_INCH).toInt() - margin_top - margin_bottom
                if (mRenderPageHeight != contentHeight) {
                    mRenderPageHeight = contentHeight
                    shouldLayout = true
                }

                val isPortrait = currentAttributes!!.mediaSize!!.isPortrait
                if (mIsPortrait != isPortrait) {
                    mIsPortrait = isPortrait
                    shouldLayout = true
                }

                if (!shouldLayout) {
                    Log.d("TEST", "onLayout() - Finished. No Re-Layout required.")
                    callback.onLayoutFinished(printDocumentInfo, false)
                    return
                }

                // Compute the expected number of printed pages
                // totalPages =  computePageCount(currentAttributes);
                totalPages = 1 // I draw only 1 page for demo

                printDocumentInfo = PrintDocumentInfo.Builder("print_output.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(totalPages)
                        .build()

                // Content layout reflow is complete
                callback.onLayoutFinished(printDocumentInfo, true)
                Log.d("TEST", "onLayout f")
            }

            override fun onWrite(pages: Array<PageRange>, destination: ParcelFileDescriptor, cancellationSignal: CancellationSignal, callback: PrintDocumentAdapter.WriteResultCallback) {
                Log.d("TEST", "onWrite s")

                // Create a new PdfDocument with the requested page attributes
                val pdfAttributes = PrintAttributes.Builder()
                        .setMediaSize(currentAttributes!!.mediaSize!!)
                        .setResolution(currentAttributes!!.resolution!!)
                        .setMinMargins(PrintAttributes.Margins(
                                margin_left * MILS_PER_INCH / 72,
                                margin_top * MILS_PER_INCH / 72,
                                margin_right * MILS_PER_INCH / 72,
                                margin_bottom * MILS_PER_INCH / 72
                        )).build()
                var mPdfDocument: PrintedPdfDocument? = PrintedPdfDocument(mParentActivity, pdfAttributes)

                val writtenPages = SparseIntArray()

                // Iterate over each page of the document, check if it's in the output range.
                for (i in 0 until totalPages) {
                    // Check to see if this page is in the output range.
                    if (containsPage(pages, i)) {
                        // If so, add it to writtenPagesArray. writtenPagesArray.size()
                        // is used to compute the next output page index.
                        writtenPages.append(writtenPages.size(), i)
                        val page = mPdfDocument!!.startPage(i)

                        // check for cancellation
                        if (cancellationSignal.isCanceled) {
                            callback.onWriteCancelled()
                            mPdfDocument.close()
                            mPdfDocument = null
                            return
                        }

                        // Draw page content for printing
                        drawPage(page, currentAttributes!!)

                        // Rendering is complete, so page can be finalized.
                        mPdfDocument.finishPage(page)
                    }
                }

                // Write PDF document to file
                try {
                    mPdfDocument!!.writeTo(FileOutputStream(
                            destination.fileDescriptor))
                } catch (e: IOException) {
                    callback.onWriteFailed(e.toString())
                    return
                } finally {
                    mPdfDocument!!.close()
                    mPdfDocument = null
                }

                val writtenPageRange = computeWrittenPageRanges(writtenPages)
                // Signal the print framework the document is complete
                callback.onWriteFinished(writtenPageRange)
                Log.d("TEST", "onWrite f")

            }


            /**
             * Render method
             *
             */
            private fun computePageCount(printAttributes: PrintAttributes): Int {
                return 1
            }

            /**
             * Render method
             */
            private fun drawPage(page: PdfDocument.Page, printAttributes: PrintAttributes) {
                // place picture in pdf file
                val canvasPage = page.canvas

                // useful
                val isPortrait = printAttributes.mediaSize!!.isPortrait

                // units are in points (1/72 of an inch)
                // create picture printable size
                val bmp = Bitmap.createBitmap(canvasPage.width, canvasPage.height, Bitmap.Config.ARGB_8888)

                // draw border for example in printable
                val canvasBmp = Canvas(bmp)

                val paint = Paint()
                paint.color = Color.BLACK
                canvasBmp.drawRect(0f, 0f, canvasBmp.width.toFloat(), canvasBmp.height.toFloat(), paint)

                paint.color = Color.WHITE
                val borderWidth = 3f
                canvasBmp.drawRect(borderWidth, borderWidth,
                        canvasBmp.width - borderWidth,
                        canvasBmp.height - borderWidth,
                        paint)

                paint.color = Color.BLACK
                paint.textSize = 11f
                canvasBmp.drawText("size " + canvasBmp.width.toString() + "x" + canvasBmp.height.toString(), 24f, 24f, paint)
                canvasBmp.drawText("media " + currentAttributes!!.mediaSize!!.id, 54f, 150f, paint)
                paint.textSize = 24f
                canvasBmp.drawText("Hello, BMP!", 54f, 50f, paint)

                canvasPage.drawBitmap(bmp, 0f, 0f, null)

                // direct on page

                paint.color = Color.BLACK
                paint.textSize = 24f
                canvasPage.drawText("Hello, PAGE!", 54f, 100f, paint)


            }

            /**
             * Function that converts the selected pages to be written in form of PageRange array.
             * @param writtenPages a SparseIntArray that contains the pages that must be written.
             * @return a PageRange array containing the resulting ranges.
             */
            private fun computeWrittenPageRanges(writtenPages: SparseIntArray): Array<PageRange?> {
                val pageRanges = ArrayList<PageRange>()

                var start = -1
                var end: Int
                val writtenPageCount = writtenPages.size()
                var i = 0
                while (i < writtenPageCount) {
                    if (start < 0) {
                        start = writtenPages.valueAt(i)
                    }
                    end = start
                    var oldEnd = end
                    while (i < writtenPageCount && end - oldEnd <= 1) {
                        oldEnd = end
                        end = writtenPages.valueAt(i)
                        i++
                    }
                    if (start >= 0) {
                        val pageRange = PageRange(start, end)
                        pageRanges.add(pageRange)
                    }
                    start = -1
                    i++
                }

                val pageRangesArray = arrayOfNulls<PageRange>(pageRanges.size)
                pageRanges.toTypedArray()
                return pageRangesArray
            }

            /**
             * Checks if a given page number is contained in a PageRange array.
             * @param pageRanges The PageRange array of written pages.
             * @param numPage The page number to check against the PageRange array.
             * @return true if the page is contained in the PageRange array. False otherwise.
             */
            private fun containsPage(pageRanges: Array<PageRange>, numPage: Int): Boolean {
                for (pr in pageRanges) {
                    if (numPage >= pr.start && numPage <= pr.end) return true
                }
                return false
            }

            fun print(printManager: PrintManager) {
                val jobName = getString(R.string.app_name) + " Document"
                val printAttributes = PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .setResolution(PrintAttributes.Resolution("Default", "72dpi: 1pt=1dot", 72, 72))
                        .setMinMargins(PrintAttributes.Margins(500, 500, 500, 500))
                        .build()
                setCurrentAttributes(printAttributes)
                printManager.print(jobName, this, printAttributes)
            }

            /*companion object {

                private val MILS_PER_INCH = 1000
            }*/
        }


        //create object of print adapter
        val printAdapter = DemoDocumentAdapter(this!!.activity!!)
        //create object of print manager in your device
        val printManager = context!!.getSystemService(Context.PRINT_SERVICE) as PrintManager?
        if (printManager != null) {
            printAdapter.print(printManager)
        } else {
            Toast.makeText(context, "Print service not available", Toast.LENGTH_LONG).show()
        }

        button.text = "x"
    }

/*
    fun print(connection:UsbDeviceConnection, usbInterface:UsbInterface) {
        val test = ed_txt.getText().toString() + "\n\n"
        testBytes = test.toByteArray()
        if (usbInterface == null)
        {
            Toast.makeText(context, "INTERFACE IS NULL", Toast.LENGTH_SHORT).show()
        }
        else if (connection == null)
        {
            Toast.makeText(context, "CONNECTION IS NULL", Toast.LENGTH_SHORT).show()
        }
        else if (forceCLaim == null)
        {
            Toast.makeText(context, "FORCE CLAIM IS NULL", Toast.LENGTH_SHORT).show()
        }
        else
        {
            connection.claimInterface(usbInterface,
                    forceCLaim)
            val thread = Thread(object:Runnable {
                 override fun run() {
                    val cut_paper = byteArrayOf(0x1D, 0x56, 0x41, 0x10)
                    connection.bulkTransfer(
                            mEndPoint, testBytes, testBytes!!.size, 0)
                    connection.bulkTransfer(
                            mEndPoint, cut_paper, cut_paper.size, 0)
                }
            })
            thread.run()
        }
    }


    val mUsbReceiver: BroadcastReceiver = object:BroadcastReceiver() {
        override fun onReceive(context:Context, intent:Intent) {
            val action = intent.getAction()
            if (ACTION_USB_PERMISSION.equals(action))
            {
                synchronized (this) {
                    val device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE) as UsbDevice
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                    {
                        if (device != null)
                        {
                            //call method to set up device communication

                            mInterface = device.getInterface(0)
                            mEndPoint = mInterface!!.getEndpoint(1)// 0 IN and 1 OUT to printer.
                            mConnection = mUsbManager!!.openDevice(device)
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "PERMISSION DENIED FOR THIS DEVICE", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }




    private fun translateDeviceClass(deviceClass:Int):String {
        when (deviceClass) {
            UsbConstants.USB_CLASS_APP_SPEC ->
                return "Application specific USB class"
            UsbConstants.USB_CLASS_AUDIO ->
                return "USB class for audio devices"
            UsbConstants.USB_CLASS_CDC_DATA ->
                return "USB class for CDC devices (communications device class)"
            UsbConstants.USB_CLASS_COMM ->
                return "USB class for communication devices"
            UsbConstants.USB_CLASS_CONTENT_SEC ->
                return "USB class for content security devices"
            UsbConstants.USB_CLASS_CSCID ->
                return "USB class for content smart card devices"
            UsbConstants.USB_CLASS_HID ->
                return "USB class for human interface devices (for example, mice and keyboards)"
            UsbConstants.USB_CLASS_HUB ->
                return "USB class for USB hubs"
            UsbConstants.USB_CLASS_MASS_STORAGE ->
                return "USB class for mass storage devices"
            UsbConstants.USB_CLASS_MISC ->
                return "USB class for wireless miscellaneous devices"
            UsbConstants.USB_CLASS_PER_INTERFACE ->
                return "USB class indicating that the class is determined on a per-interface basis"
            UsbConstants.USB_CLASS_PHYSICA ->
                return "USB class for physical devices"
            UsbConstants.USB_CLASS_PRINTER ->
                return "USB class for printers"
            UsbConstants.USB_CLASS_STILL_IMAGE ->
                return "USB class for still image devices (digital cameras)"
            UsbConstants.USB_CLASS_VENDOR_SPEC ->
                return "Vendor specific USB class"
            UsbConstants.USB_CLASS_VIDEO ->
                return "USB class for video devices"
            UsbConstants.USB_CLASS_WIRELESS_CONTROLLER ->
                return "USB class for wireless controller devices"
            else ->
                return "Unknown USB class!"
        }
    }


*/

/*
    fun callTickets(){

        val db = FirebaseFirestore.getInstance()
        db.collection("ticketprices")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        println("getTickets ${document.id} => ${document.data}")

                        val callTickets = document.toObject(TicketPrices_Document::class.java)

                        println("printChild ${callTickets.price}")

                        val builder = LatLngBounds.Builder()
                        ListTickets.add(
                                TicketPrices_Document(
                                        price = callTickets.price,
                                        source = callTickets.source,
                                        target = callTickets.target,
                                        userid = callTickets.userid
                                )
                        )
                        callAdpater()
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getDocuments")
                }
    }

    private fun callAdpater() {

        var myNotesAdapter= MyNotesAdpater(context!!, ListTickets)
        mlistView.adapter=myNotesAdapter

        println("printAdapter ${myNotesAdapter.count}")

    }

    inner class  MyNotesAdpater: BaseAdapter {
        var listNotesAdpater= ArrayList<TicketPrices_Document>()
        var context: Context?=null
        constructor(context: Context, listNotesAdpater: ArrayList<TicketPrices_Document>):super(){
            this.listNotesAdpater=listNotesAdpater
            this.context=context
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {


            var myView=layoutInflater.inflate(R.layout.fragment_ticket,null)
            var myNote=listNotesAdpater[p0]

            myView.btnTicket.text = "$ "+myNote.price.toString()
            myView.txtOrigDes.text = myNote.source+" - "+myNote.target


            println("printTest ${myNote.price}")


            myView.btnTicket.setOnClickListener {

                sessionStorage.price = myNote.price
                sessionStorage.source = myNote.source
                sessionStorage.target = myNote.target
                RegisterTicket()
            }


            myView.btnCamera.setOnClickListener{
                //takePhotoByCamera()
                run {
                    IntentIntegrator(getActivity()).initiateScan()

                }

            }

            return myView
        }



        override fun getItem(p0: Int): Any {



            return listNotesAdpater[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {

            return listNotesAdpater.size

        }


    }

    fun RegisterTicket(){

        var everyDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(Date())

        val db = FirebaseFirestore.getInstance()
        val city = hashMapOf(

                "carid" to sessionStorage.carId,
                "date" to everyDate,
                "driverid" to sessionStorage.userId,
                "price" to sessionStorage.price,
                "routeid" to "iQjJ5p7S3jXegg0WWTMi",
                "source" to sessionStorage.source,
                "target" to sessionStorage.target,
                "travelid" to "20180704-15",
                "userid" to "oBFp51RhsCpBrgwBNjLr",
                "latitude" to sessionStorage.latitud,
                "longitude" to sessionStorage.longitud
        )
        db.collection("ticketsales").add(city)
                .addOnSuccessListener {
                    println("add Information") }
                .addOnFailureListener { e ->
                    println("Error writing document $e") }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){

            if(result.contents != null){
                scannedResult = result.contents
                txtValue.text = scannedResult
            } else {
                txtValue.text = "scan failed"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putString("scannedResult", scannedResult)
        super.onSaveInstanceState(outState)
    }
*/
}
