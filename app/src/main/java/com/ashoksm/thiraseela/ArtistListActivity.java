package com.ashoksm.thiraseela;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ashoksm.thiraseela.adapter.ArtistListAdapter;
import com.ashoksm.thiraseela.vo.ArtistListVO;

import java.util.ArrayList;
import java.util.List;

public class ArtistListActivity extends ActionBarActivity {

    public static final String EXTRA_PERFORMER_NAME = "EXTRA_PERFORMER_NAME";

    public static final List<ArtistListVO> ARTIST_LIST_VOS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.artist_list_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //set adapter
        ARTIST_LIST_VOS.clear();
        ARTIST_LIST_VOS.add(new ArtistListVO("Methil Devika", "Dancer | Scholar & Educator", "http://thiraseela.com/gleimo/performers/images/perfomr72/thumb/Perfmr_img.jpeg", "http://thiraseela.com/gleimo/performers/images/perfomr72/Perfmr_img.jpeg", "Training in Bharathanatyam from Kalaimamani S. Natarajan, who belongs to the illustrious family that upholds the annual Melattur Bhagavatha Mela Natakam in Tanjavur. Training in Kuchipudi in Chennai from distinguished gurus Guru Dr. Vempati Chinna Satyam and Guru Vempati Ravi, Kuchipudi Dance academy. Also trained under Smt. Satyapriya Ramana of the Narthanasaala School of Kuchipudi. Training in Mohiniyattom under renowned Gurus at the Regatta Cultural Society, Thiruvananthapuram.", "others, Palakkad"));
        ARTIST_LIST_VOS.add(new ArtistListVO("Margi Sathi", "Nangiarkoothu & Kutiyattam", "http://thiraseela.com/gleimo/performers/images/perfomr52/thumb/Perfmr_img.jpeg", "http://thiraseela.com/gleimo/performers/images/perfomr52/Perfmr_img.jpeg", "Margi Sathi has performed Nangiarkoothu,the solo part of Kutiyattam as well as the female roles in Kutiyattam in France, Spain , USA, Germany, Italy, Switzerland etc.\n" +
                "\n" +
                "Her last performance was at he UNESCO Headquarters in Paris on October 2001, before a specially invited assembly of more than 500 guests from all over the world, to mark the UNESCO proclamation of Kutiyattam as a World Heritage Art."
                , "karamana, Trivandrum"));
        ARTIST_LIST_VOS.add(new ArtistListVO("Sasikala S. Vellodi", "Kathakali Artist", "http://thiraseela.com/gleimo/performers/images/perfomr54/thumb/Perfmr_img.jpeg", "http://thiraseela.com/gleimo/performers/images/perfomr54/Perfmr_img.jpeg", "Born in Cochin, Kerala Sasikala started her career in dance with Mohiniyattam. Later her affection towards Kathakali led her to be associated with the Kathakali maestro Kalamandalam E. Vasudevan Nair. Later for her higher studies she got her intense training under Late Padmashree Shri. Kalamandalam Krishnan Nair.\n" +
                "\n" +
                "She had performed with all the stalwarts of Kathakali in leading roles of the story always. Her Lalita, Mohini, Chitralekha, Kunti and Damayanti are still alive in the audience’s pulse. An ‘A’ grade artist in Thiruvananthapuram Doordarshan, Sasikala bagged many prizes in the University festivals of Kerala during her collage days. She is a post-graduate in Commerce.\n"
                , "Kochi, Ernakulam"));
        ARTIST_LIST_VOS.add(new ArtistListVO("Kirti Ramgopal", "Bharathanatyam Dancer", "http://thiraseela.com/gleimo/performers/images/perfomr92/thumb/Perfmr_img.jpeg", "http://thiraseela.com/gleimo/performers/images/perfomr92/Perfmr_img.jpeg", "Bharathanatyam, the supreme Indian classical dance that skillfully embodies the three primary ingredients of Dance - Rhythm, Rhyme and Expression, fascinated Kirti Ramgopal at a tender age. With her passion, focus and rigorous training, she is acclaimed as one of the brightest talents in her generation.\n" +
                "\n" +
                "Kirti began her training under Smt.Padmini Ramachandran and has also been mentored and guided by Smt. Priyadarshini Govind and Sri.A. Lakshman. Currently she is undergoing training in Abhinaya under Smt. Bragha Bessell.\n" +
                "\n" +
                "With her keen ability to understand and imbibe the nuances of this beautiful art form and with her perseverance and dedication, she has traveled across India and internationally throughout US, Canada, UK, Europe, The Middle East & Bangladesh performing before discerning audiences and winning acclaim from connoisseurs of art and critics alike.\n" +
                "\n" +
                "Kirti creates incandescent Natyam with her abundance of lively skill, geometric aptness, nimbleness and grace and sensitivity in delivery. Her dancing has been described as vibrant, expressive and that which extends a spirit of Joy.\n" +
                "\n" +
                "It is her sincere effort to delve further into this wonderful art of Bharathanatyam and preserve and propagate its pristine beauty.\n", "Cooke Town, Bangalore"));
        ARTIST_LIST_VOS.add(new ArtistListVO("Shobana", "Film actress and Bharatanatyam dancer", "http://thiraseela.com/gleimo/performers/images/perfomr135/thumb/Perfmr_img.jpeg", "http://thiraseela.com/gleimo/performers/images/perfomr135/Perfmr_img.jpeg", "Kalarpana – Institute of Bharatanatyam", "Chennai, Tamil Nadu"));
        ArtistListAdapter adapter = new ArtistListAdapter(ARTIST_LIST_VOS, this);
        mRecyclerView.setAdapter(adapter);


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), ArtistDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(EXTRA_PERFORMER_NAME, String.valueOf(position));
                        getApplicationContext().startActivity(intent);
                    }
                })
        );
    }
}
