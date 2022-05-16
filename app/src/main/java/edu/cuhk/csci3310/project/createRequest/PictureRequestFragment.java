package edu.cuhk.csci3310.project.createRequest;

import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import edu.cuhk.csci3310.project.R;

public class PictureRequestFragment extends RequestFragment {

    // Views
    ArrayList<ImageView> mImageViewList;
    ImageView mExpandedImageView;
    Button mImageButton;

    // https://developer.android.com/topic/libraries/architecture/viewmodel?gclid=EAIaIQobChMI5ZaZmtnX9wIVF9dMAh06UQjZEAAYASAAEgJ90vD_BwE&gclsrc=aw.ds#java
    // https://www.youtube.com/watch?v=orH4K6qBzvE
    // https://stackoverflow.com/questions/44998051/cannot-create-an-instance-of-class-viewmodel
    // https://stackoverflow.com/questions/61462278/cannot-create-an-instance-of-class-of-androidviewmodel-in-android
    // https://stackoverflow.com/questions/50969022/cannot-create-an-instance-of-class-viewmodel
    //     implementation "android.arch.lifecycle:viewmodel:1.1.1"
//    public class PictureViewModel extends ViewModel {
//        public Bitmap picture;
//        public PictureViewModel() { }
//        //public PictureViewModel newInstance() { return new PictureViewModel();}
//    }
//    PictureViewModel pictureViewModel;

    private final ActivityResultLauncher<Intent> pictureActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // https://developer.android.com/training/camera/photobasics#java
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // data.getClipData();
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            // requestPicture = imageBitmap;
                            mImageViewList.get(0).setImageBitmap(imageBitmap);
                            //pictureViewModel.picture = imageBitmap;
                        }
                    }
                }
            });


    public PictureRequestFragment() {
        // Required empty public constructor
    }

    public static PictureRequestFragment newInstance(ImageView imageView) {
        PictureRequestFragment fragment = new PictureRequestFragment();
        //fragment.mImageViewList = new ArrayList<>();
        fragment.mExpandedImageView = imageView;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageViewList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_picture, container, false);
        mImageViewList.add(view.findViewById(R.id.picture_recycler));
        mImageButton = view.findViewById(R.id.picture_button);
        if (mExpandedImageView == null)
            mExpandedImageView = getActivity().findViewById(R.id.expanded_image);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //pictureViewModel = new ViewModelProvider(requireActivity()).get(PictureViewModel.class);
        mExpandedImageView.setImageDrawable(null);

        for (ImageView imageView : mImageViewList) {
            //imageView.setImageDrawable(null);
            //imageView.setImageBitmap(pictureViewModel.picture);
            setZoomableImageView(imageView, mExpandedImageView);
        }

        setRetrievePictureButtonView(mImageButton, pictureActivityLauncher);

    }

    public static void setZoomableImageView(ImageView imageView, ImageView expandedImageView){
        // resize fragment
        // https://stackoverflow.com/questions/10699464/dynamic-resize-fragment-android
        // https://developer.android.com/guide/fragments/animate
        // https://developer.android.com/training/animation/zoom#java

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((ImageView) view).getDrawable() == null)
                    return;
                expandedImageView.setClickable(true);
                expandedImageView.setVisibility(View.VISIBLE);
                expandedImageView.setImageDrawable(((ImageView) view).getDrawable());
            }
        });

        if (!expandedImageView.hasOnClickListeners()) {
            expandedImageView.setClickable(false);
            expandedImageView.setVisibility(View.INVISIBLE);

            expandedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setClickable(false);
                    view.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    public void setRetrievePictureButtonView(Button buttonView, ActivityResultLauncher<Intent> activityResultLauncher){

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // https://stackoverflow.com/questions/25524617/intent-to-choose-between-the-camera-or-the-gallery-in-android
                Intent pictureChooserIntent = new Intent(Intent.ACTION_CHOOSER);
                ArrayList<Intent> intentArrayList = new ArrayList<Intent>();

                Intent getPictureIntent = new Intent(Intent.ACTION_PICK);
                getPictureIntent.setType("image/*");
                // getPictureIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                pictureChooserIntent.putExtra(Intent.EXTRA_INTENT, getPictureIntent);
                pictureChooserIntent.putExtra(Intent.EXTRA_TITLE, "Select from:");

                // Cannot be static
                if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentArrayList.add(cameraIntent);
                }

                pictureChooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArrayList.toArray(new Intent[0]));
                intentArrayList.clear();

                try {
                    activityResultLauncher.launch(pictureChooserIntent);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle savedInstanceState){
        Bitmap[] images = new Bitmap[mImageViewList.size()];
        for (int i=0; i < images.length; i++){
            images[i] = getBitmapFromView(mImageViewList.get(i));
        }
        savedInstanceState.putParcelableArray("image", images);
    }

    @Override
    protected void onLoadInstanceState(@Nullable Bundle savedInstanceState){
        //Bitmap[] images = (Bitmap[]) savedInstanceState.getParcelableArray("image");
        Parcelable[] images = savedInstanceState.getParcelableArray("image");
        for (int i=0; i < images.length; i++)
            mImageViewList.get(i).setImageBitmap((Bitmap) images[i]);
    }

    @Override
    public boolean isFilled(){
        if (mImageViewList.size() > 0){
            return mImageViewList.get(0).getDrawable() != null;
        }
        return false;
    }

    public static Bitmap getBitmapFromView(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if (drawable != null)
            return ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        return null;
    }

    public Bitmap getInformationBitmap(){
        return getBitmapFromView(mImageViewList.get(0));
    }

    public ArrayList<Bitmap> getInformationBitmapList(){
        ArrayList<Bitmap> output = new ArrayList<>();
        for (ImageView imageView : mImageViewList) {
            output.add(getBitmapFromView(imageView));
        }
        return output;
    }

    public ArrayList<byte[]> getInformationByteArray(){
        // https://stackoverflow.com/questions/4352172/how-do-you-pass-images-bitmaps-between-android-activities-using-bundles
        ArrayList<byte[]> output = new ArrayList<>();
        for (ImageView imageView : mImageViewList) {
            Bitmap bitmap = getBitmapFromView(imageView);
            ByteArrayOutputStream bitmapOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, bitmapOutputStream);
            output.add(bitmapOutputStream.toByteArray()); // byte[]
            //BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("key"),0,getIntent().getByteArrayExtra("key").length);
        }
        return output;
    }


}