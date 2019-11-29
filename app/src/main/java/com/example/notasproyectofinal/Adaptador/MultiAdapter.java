package com.example.notasproyectofinal.Adaptador;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notasproyectofinal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

public class MultiAdapter extends RecyclerView.Adapter {

    private ArrayList<Adjuntos>dataSet;
    Context context;
    int total_types;
    MediaPlayer mPlayer;
    private Boolean fabStateVolume = false;

    public static class TextTypeViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView cardView;

        public TextTypeViewHolder (View itemView){
            super(itemView);

            this.textView = (TextView) itemView.findViewById(R.id.type);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ImageTypeViewHolder(View itemView){
            super(itemView);

            this.textView = (TextView) itemView.findViewById(R.id.type);
            this.imageView = (ImageView) itemView.findViewById(R.id.background);
        }
    }

    public static class AudioTypeViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        FloatingActionButton fab;

        public AudioTypeViewHolder(View itemView){
            super(itemView);
            this.fab = (FloatingActionButton) itemView.findViewById(R.id.fab);
            this.textView = (TextView) itemView.findViewById(R.id.type);
        }
    }

    public static class VideoTyoeViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        VideoView videoView;

        public VideoTyoeViewHolder(View itemView){
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.type);
            this.videoView =(VideoView) itemView.findViewById(R.id.play_it);
        }
    }

    public MultiAdapter(ArrayList<Adjuntos>data,Context context){
        this.dataSet = data;
        this.context = context;
        total_types = dataSet.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case Adjuntos.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_type,parent,false);
                return new ImageTypeViewHolder(view);
            case Adjuntos.AUDIO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_type,parent,false);
                return new AudioTypeViewHolder(view);
            case Adjuntos.VIDEO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_type,parent,false);
                return new VideoTyoeViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        switch (dataSet.get(position).type){
            case 0:
                return Adjuntos.IMAGE_TYPE;
            case 1:
                return Adjuntos.AUDIO_TYPE;
            case 2:
                return Adjuntos.VIDEO_TYPE;
            default:
                return -1;
        }
        //return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Adjuntos objeto = dataSet.get(position);
        if (objeto != null){
            switch (objeto.type){
                case Adjuntos.IMAGE_TYPE:
                    ((ImageTypeViewHolder) holder).textView.setText(objeto.text);
                    ((ImageTypeViewHolder) holder).imageView.setImageURI(objeto.data);
                    break;
                case Adjuntos.AUDIO_TYPE:

                    ((AudioTypeViewHolder) holder).textView.setText(objeto.text);

                    ((AudioTypeViewHolder) holder).fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (fabStateVolume) {
                                if (mPlayer.isPlaying()) {
                                    mPlayer.stop();

                                }
                                //((AudioTypeViewHolder) holder).fab.setImageResource(R.drawable.volume);
                                //fabStateVolume = false;

                            } else {
                                mPlayer = new MediaPlayer();
                                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                try {
                                    mPlayer.setDataSource(context,objeto.data);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    mPlayer.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //mPlayer.setLooping(true);
                                mPlayer.start();
                                fabStateVolume = true;

                            }
                        }
                    });
                    break;
                case Adjuntos.VIDEO_TYPE:
                    ((VideoTyoeViewHolder) holder).textView.setText(objeto.text);
                    ((VideoTyoeViewHolder) holder).videoView.setVideoURI(objeto.data);
                    MediaController mediaController = new MediaController(context);
                    mediaController.setAnchorView(((VideoTyoeViewHolder)holder).videoView);
                    ((VideoTyoeViewHolder) holder).videoView.setMediaController(mediaController);
                    ((VideoTyoeViewHolder) holder).videoView.seekTo(10);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
