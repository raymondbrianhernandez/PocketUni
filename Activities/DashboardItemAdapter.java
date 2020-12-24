package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import raymondhernandez.pocketuniv.Model.Book;
import raymondhernandez.pocketuniv.Model.Course;
import raymondhernandez.pocketuniv.Model.Professor;
import raymondhernandez.pocketuniv.R;

/**
 * Created by Chachi on 4/7/2016.
 */
public class DashboardItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Object> itemList;
    Context mContext;

    public void setItemList(ArrayList<Object> itemList) {
        this.itemList = itemList;
    }

    public DashboardItemAdapter(Context context, ArrayList<Object> itemList) {
        this.mContext = context;
        this.itemList = itemList;
    }

    public static class ProfessorViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView item_name,item_bio, item_dept, item_courses;
        public RatingBar item_rating;
        public ImageView item_photo;
        public AppCompatImageButton details, call, mail;
        public String email;
        public Long phone;

        public ProfessorViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            item_dept = (TextView) itemView.findViewById(R.id.item_dept);
            item_courses = (TextView) itemView.findViewById(R.id.item_courses);
            item_photo = (ImageView) itemView.findViewById(R.id.item_photo);
            item_rating = (RatingBar) itemView.findViewById(R.id.item_rating);
            item_bio = (TextView) itemView.findViewById(R.id.item_bio);
            details = (AppCompatImageButton) itemView.findViewById(R.id.btn_details);
            call = (AppCompatImageButton) itemView.findViewById(R.id.btn_call);
            mail = (AppCompatImageButton) itemView.findViewById(R.id.btn_email);
        }
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        public CardView bookCardView;
        public TextView book_name, book_author, book_courses;
        public RatingBar book_rating;
        public ImageView book_photo;
        public AppCompatImageButton book_details;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookCardView = (CardView) itemView.findViewById(R.id.cardview_book);
            book_name = (TextView) itemView.findViewById(R.id.book_name);
            book_author = (TextView) itemView.findViewById(R.id.book_author);
            book_courses = (TextView) itemView.findViewById(R.id.book_courses);
            book_rating = (RatingBar) itemView.findViewById(R.id.book_rating);
            book_photo = (ImageView) itemView.findViewById(R.id.book_photo);
            book_details = (AppCompatImageButton) itemView.findViewById(R.id.btn_book_details);
        }
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder{
        public CardView courseCardView;
        public TextView courseName,courseCode;

        public CourseViewHolder(View itemView) {
            super(itemView);
            courseCardView = (CardView)itemView.findViewById(R.id.cardview_course);
            courseName = (TextView)itemView.findViewById(R.id.course_name);
            courseCode = (TextView)itemView.findViewById(R.id.course_code);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof Professor) {
            return 0;
        } else if (itemList.get(position) instanceof Book) {
            return 1;
        }else if (itemList.get(position) instanceof Course){
            return 2;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 0:
                View v0 = inflater.inflate(R.layout.cardview_professor, parent, false);
                return new ProfessorViewHolder(v0);
            case 1:
                View v1 = inflater.inflate(R.layout.cardview_book, parent, false);
                return new BookViewHolder(v1);
            case 2:
                View v2 = inflater.inflate(R.layout.cardview_course, parent, false);
                return new CourseViewHolder(v2);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ProfessorViewHolder professorViewHolder = (ProfessorViewHolder) holder;
                configureProfessorViewHolder(professorViewHolder, position);
                break;
            case 1:
                BookViewHolder bookViewHolder = (BookViewHolder) holder;
                configureBookViewHolder(bookViewHolder, position);
                break;
            case 2:
                CourseViewHolder courseViewHolder = (CourseViewHolder) holder;
                configureCourseViewHolder(courseViewHolder, position);
                break;
        }


    }

    public void toggleView(View view) {
        if (view.getVisibility() == View.GONE)
            view.setVisibility(View.VISIBLE);
        else if (view.getVisibility() == View.VISIBLE)
            view.setVisibility(View.GONE);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void configureProfessorViewHolder(final ProfessorViewHolder holder, int position) {

        Professor p = (Professor) itemList.get(position);
        final String[] courseArray = p.getCourses();
        String subj = "Courses:";
        for (int i = 0; i < courseArray.length; i++) {
            subj = subj + courseArray[i] + " ";
        }
        holder.item_name.setText(p.getName());
        holder.item_dept.setText(p.getDepartment());
//        holder.item_courses.setText(subj.trim());
        holder.item_rating.setRating(p.getRating());
        Picasso.with(mContext)
                .load(p.getImageUrl())
                .into(holder.item_photo);
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(holder.item_bio);
            }
        });

        holder.email = p.getEmail();
        holder.phone = p.getPhoneNumber();
        holder.item_bio.setText(p.getBiography());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.phone != null) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + holder.phone.toString()));
                    mContext.startActivity(callIntent);
                }
            }
        });

        holder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND,Uri.fromParts("mailto",holder.email,null));
                try {
                    mContext.startActivity(emailIntent);
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mContext, "Uh...No email app?", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void configureBookViewHolder(final BookViewHolder holder, int position) {
        Book book = (Book) itemList.get(position);
        holder.book_name.setText(book.getTitle());
        holder.book_author.setText(book.getAuthor());
        holder.book_rating.setRating(book.getRating());
        Picasso.with(mContext)
                .load("http://www.chrislong365.com/professors/non_photo.jpg")
                .into(holder.book_photo);

    }

    public void configureCourseViewHolder(final CourseViewHolder holder,int position){
        Course course = (Course)itemList.get(position);
        holder.courseName.setText(course.getCourseName());
        holder.courseCode.setText(course.getCourseCode());

        holder.courseCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RatingCourseActivity.class);
                intent.putExtra("CourseCode", holder.courseCode.getText());
                mContext.startActivity(intent);
            }
        });
    }
}
