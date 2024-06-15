package edu.esprit.controllers.camera;

import edu.esprit.entities.EndUser;
import edu.esprit.services.ServiceUser;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends JFrame {

    private JLabel cameraScreen;
    public VideoCapture capture;

    private JButton btnCapture;
    private Mat image;
    private boolean clicked = false;

    public volatile boolean isCameraOpen = true;


//    ServiceUser serviceUser = new ServiceUser();
//
//    EndUser endUser = new EndUser();

    private String url;

    public Camera() {
        // design ui
        setLayout(null);

        cameraScreen = new JLabel();
        cameraScreen.setBounds(0, 0, 640, 480);
        add(cameraScreen);

        btnCapture = new JButton("Capture");
        btnCapture.setBounds(300, 480, 80, 40);
        add(btnCapture);

        btnCapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = true;
            }
        });

        setSize(new Dimension(640, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

//    public void startCamera() {
//        capture = new VideoCapture(0); // Adjust the index if needed
//
//        if (!capture.isOpened()) {
//            System.out.println("Error: Could not open the camera.");
//            return;
//        }
//
//        image = new Mat();
//        byte[] imageData;
//
//        ImageIcon icon;
//        while (true) {
//            if (capture.read(image)) {
//                final MatOfByte buf = new MatOfByte();
//                Imgcodecs.imencode(".jpg", image, buf);
//
//                imageData = buf.toArray();
//                icon = new ImageIcon(imageData);
//                cameraScreen.setIcon(icon);
//
//                //capture and save to file
//                if(clicked){
//                    //prompt for the enter file
//                    String name = JOptionPane.showInputDialog(this,"Enter image name");
//                    if(name == null){
//                        name = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
//                    }
//                    //write to file
//                    Imgcodecs.imwrite("src/main/resources/images/" + name + ".jpg",image);
//                    clicked = false;
//
//                    //get url
//                    url = "src/main/resources/images/" + name + ".jpg";
//                    endUser.setImage("src/main/resources/images/" + name + ".jpg");
//                }
//            } else {
//                System.out.println("Error: Couldn't read the frame from the camera.");
//            }
//        }
//    }

    // Define a callback interface
    public interface CameraCallback {
        void onImageSaved(String imageUrl);
    }

    private CameraCallback callback;

    // Set the callback
    public void setCallback(CameraCallback callback) {
        this.callback = callback;
    }

    public void startCamera() {
        capture = new VideoCapture(0); // Adjust the index if needed

        if (!capture.isOpened()) {
            System.out.println("Error: Could not open the camera.");
            return;
        }

        image = new Mat();
        byte[] imageData;

        ImageIcon icon;
        while (isCameraOpen) {
            if (capture.read(image)) {
                final MatOfByte buf = new MatOfByte();
                Imgcodecs.imencode(".jpg", image, buf);

                imageData = buf.toArray();
                icon = new ImageIcon(imageData);

                cameraScreen.setIcon(icon);

                // capture and save to file
                if (clicked) {
                    // prompt for the enter file
                    String name = JOptionPane.showInputDialog(this, "Enter image name");
                    if (name == null) {
                        name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
                    }
                    // write to file
                    Imgcodecs.imwrite("src/main/resources/images/" + name + ".jpg", image);
                    clicked = false;

                    // get url
                    url = "src/main/resources/images/" + name + ".jpg";
//                    endUser.setImage("src/main/resources/images/" + name + ".jpg");
                    System.out.println(url);

                    // Close the camera after saving the image
                    isCameraOpen = false;

                    // Notify the callback with the image URL
                    if (callback != null) {
                        callback.onImageSaved(url);
                    }

                }
            } else {
                System.out.println("Error: Couldn't read the frame from the camera.");
            }
        }

        // Release the camera resources
        capture.release();
        System.out.println("Camera closed.");
    }


//    public void startCamera(){
//        capture = new VideoCapture();
//        image = new Mat();
//        byte[] imageData;
//
//        ImageIcon icon;
//        while (true){
//            //read image to matrix
//            capture.read(image);
//
//            //convert matrix to byte
//            final MatOfByte buf = new MatOfByte();
//            Imgcodecs.imencode("jpg",image,buf);
//
//            imageData = buf.toArray();
//            //add to JLabel
//            icon = new ImageIcon(imageData);
//            cameraScreen.setIcon(icon);
//
//            //capture and save to file
//            if(clicked){
//                //prompt for the enter file
//                String name = JOptionPane.showInputDialog(this,"Enter image name");
//                if(name == null){
//                    name = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
//                }
//                //write to file
//                Imgcodecs.imwrite("src/main/resources/images/" + name + ".jpg",image);
//                clicked = false;
//            }
//
//        }
//    }


    public static void main() {
        // Load OpenCV DLL
        System.load("C:/Users/werta/Documents/GitHub/baladity/src/main/java/edu/esprit/services/opencv_java490.dll");

        // Run camera in the Event Dispatch Thread
        EventQueue.invokeLater(() -> {
            Camera camera = new Camera();

            // Start camera in a new thread
            new Thread(() -> camera.startCamera()).start();
            camera.isCameraOpen = true;
        });
    }

    public String getUrl() {
        return this.url;
    }
}