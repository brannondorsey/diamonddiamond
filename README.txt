
Diamond Diamond Version 0.5 BETA, 4/8/13
This software has copyright by Brannon Dorsey under the CC BY-NC-SA 3.0 License. licensing information can be found at http://creativecommons.org/licenses/by-nc-sa/3.0/us/.

//----------------ABOUT-------------------

Diamond Diamond is a free artware tool that allows users to create poetic images using their own text and pictures. It aims to incorporate a balance of randomness and control over its content, leading users to draw inspiration from and make associations with imagery and language in new ways. 

//---------------USING DIAMOND DIAMOND-------------------

Diamond Diamond comes pre-loaded with a text document and a few pictures to get you started. Below you will find instructions on how to use your own content instead, but for now, just get going by opening the app. It usually takes a long time to load (thanks to the pictures) so don't worry if the window doesn't pop up right away. When it does you will see an image on screen, some scattered text, and an array of buttons. These buttons, clockwise from the top left, are [1] Camera, [2] Words reset, [3] Pixel size up, [4] Pixel size down, [5] Threshold, [6] Glitch, [7] Pixel stroke, and [8] Pixel shape. There are also two wide panel buttons at the bottom, [9] New words and [10] New image. I suggests just playing around with stuff and seeing what happens. The main things that you need to know are that the "new words" and "new image" buttons replace the on screen words and image respectively. You can drag words around on screen and make phrases, sentences, or complete garbage. Discard words you don't use by dragging them off screen. If you make something you like press the Camera button and it will save the image to the pictures folder inside of the original Diamond Diamond directory. Have fun!

The Diamond Diamond website/gallery is http://maidsoftware.wordpress.com. Please send any and all images you like (or even the ones you don't) to brannon@brannondorsey.com with "Diamond Diamond Submission" as the subject. I will be personally reviewing, and probably posting, all images I receive. We really like Diamond Diamond and we hope that you will too. Please share it with anyone you want to so that more people can like it!


//---------------BUTTON DESCRIPTIONS-------------------

Clockwise from the top left-
[1] Takes a picture of the current screen and saves it to the "pictures" folder in the directory where Diamond Diamond is located. 
[2] Resets the current words back to a random location on screen. This is helpful for if you want to reuse words that you already erased by dragging off screen. Note, this does not give you new words.
[3] Increases the size of the image pixels on screen
[4] Decreases the size of the image pixels on screen
[5] Changes all pixel values to white or black. A slider appears to the bottom left of the screen when Threshold is activate. This slider controls the number of white or black pixels. 
[6] Glitches the photograph. Try playing around with other buttons while the photo is glitched. 
[7] Creates an outline around each pixel. The slider controls the grayscale value of the outline. 
[8] Toggles the pixel shape between squares and circles. 
[9] Randomly displays five to ten words from the currently loaded text file. 
[10] Randomly displays an image from the current image_list. 


//---------------SETTING UP YOUR OWN CONTENT-------------------

1. Right click the Diamond Diamond app and select "Show Package Contents"
2. inside Diamond Diamond's package contents navigate to Contents/Resources/Java/data
3. To use your own text document, open the desired text in TextEdit. Select Format->Make Plain Text. Remove any white space (other than one character space marks) like line breaks or tabs so that the text is one large body. Save the file with the .txt extension in the writings folder in the data directory (Diamond Diamond/Contents/Resources/Java/data/writings). If the filename is more than one word do not include spaces in the file name. Use_underscores_instead. You can keep multiple files in this folder so that you easily switch back and forth between different text documents. 
4. To use your own images, replace the current folder contents with your photos. See the image specifications section for more details on the mandatory image sizes. Once the images have been copied to the images folder, select all (command-a) filenames and copy (command-c) and paste them (command-v) into the image_list.txt file located in the data folder. Overwrite the previous text (unless you kept the pre-loaded images). Make sure that each file name has the appropriate extension and that there is no white space at the top or bottom of the document. We recommend that you use twenty to one hundred images for best results. 
5. From the data directory, select the edit_this.txt file. Change "bus_east.txt" in the first line ("Name of text file : bus_east.txt") to the name of your text file including the extension. From here, you may also change the prefix that all images created using the app will be saved as. For instance you could change "image prefix: image" to "image prefix: your_name".
6. You are now ready to run the app. If the app doesn't run correctly, or your content is not as expected, see the troubleshooting section for more details.

//---------------IMAGE SPECIFICATIONS-------------------

All images must be landscape orientation. 
All images must be in JPG, TGA, and PNG file format. We highly recommend that you use .jpg.
All images must be sized as closely to 1200 x 800 at 72 ppi as possible. This should make them 90-250 KB in size. Preference should go to the images width (1200).

In order to meet these image specifications it is recommended that you resize all photos in photoshop. I suggest creating an action/droplet to batch automate your image size edits. If you do not have access to photoshop there are plenty of online resize tool like http://www.picresize.com/ for you to use. We recommend that you use between 20-80 images.

//-----------------TROUBLESHOOTING----------------------------

If Diamond Diamond doesn't open or the app doesn't work like you think its supposed to file these steps-

1. Double check that the .text file that you are trying to load is placed in the writings folder. This folder is located inside the app and can be accessed by right clicking Diamond Diamond, selecting "Show Package Contents" and then navigating inside the data folder to writings (Contents/Resources/Java/data/writings). This file name must have a .txt extension and may not contain spaces.

2. Make sure that the edit_this.txt file is inside the data folder and that it points to the correct file name in of the writing folder. It should look like this-

Name of text file : your_text_file.txt
image prefix: your_desired_image_prefix

3. Examine the image_list.txt and the images folder, both inside of the data folder. All image file names must include the appropriate extension and correspond with an image inside of the images folder. This file cannot have any white (empty) text above or below any image name. If an image is listed in image_list.txt but not included in the images folder it can crash Diamond Diamond. If there is a discrepancy remove the problem image name from image_list.txt or include the appropriate image to the images folder. image_list.txt should look something like this-

*TOP OF DOCUMENT*
IMG_0232.jpgIMG_0257.jpgIMG_0269.jpgIMG_0323.jpgIMG_0324.jpgIMG_0339.jpgIMG_0376.jpgIMG_0380.jpgIMG_0381.jpg
etc…
*BOTTOM OF DOCUMENT*

4. If you are still having problems please email me. I will be more than happy to try and fix something.


//-----------------CREDITS AND CONTACT---------------------------

Created by Brannon Dorsey (Maid Software)
Special thanks to Chris Baker 

please send any bug reports to brannon@brannondorsey.com.
Or just email me to say hi. 
