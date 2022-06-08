package ninja.la.backend;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.mongodb.client.model.Filters.eq;

@Path("/market")
public class MarketResource {

    @GET
    @Path("/{section}/names")
    @Produces("application/json")
    public String getItemName(@PathParam("section") String section) {
        MongoCollection<Document> collection;
        FindIterable<Document> documentFindIterable;

        // Testing if collection exist
        try {
            collection = ApiApplication.connect("Market", "Name_List");
        } catch (IllegalArgumentException e) {
            System.err.println("Error IllegalArgumentException : " + e.getMessage());
            throw new WebApplicationException("Error IllegalArgumentException : " + e.getMessage(), Response.Status.NOT_FOUND);
        }

        // Testing if collection is empty
        documentFindIterable = collection.find(eq("section", section));
        if (!documentFindIterable.iterator().hasNext()) {
            System.err.println("Error : Collection " + section + " is empty");
            throw new WebApplicationException("Error : Collection " + section + "is empty", Response.Status.NOT_FOUND);
        }

        return documentFindIterable.first().toJson();
    }

    @GET
    @Path("/{section}")
    @Produces("application/json")
    public String getSection(@PathParam("section") String section) {
        MongoCollection<Document> collection;
        FindIterable<Document> documentFindIterable;

        // Testing if collection exist
        try {
            collection = ApiApplication.connect("Market", section);
        } catch (IllegalArgumentException e) {
            System.err.println("Error IllegalArgumentException : " + e.getMessage());
            throw new WebApplicationException("Error IllegalArgumentException : " + e.getMessage(), Response.Status.NOT_FOUND);
        }

        // Testing if collection is empty
        documentFindIterable = collection.find();
        if (!documentFindIterable.iterator().hasNext()) {
            System.err.println("Error : Collection " + section + " is empty");
            throw new WebApplicationException("Error : Collection " + section + "is empty", Response.Status.NOT_FOUND);
        }

        return documentFindIterable.first().toJson();
    }

    @GET
    @Path("/{section}/{item}")
    @Produces("application/json")
    public String getSection(@PathParam("section") String section, @PathParam("item") String item, @QueryParam("startDate") String startDateString, @QueryParam("endDate") String endDateString) {
        MongoCollection<Document> collection;
        FindIterable<Document> documentFindIterable;
        Document document;
        Date startDate = null;
        Date endDate = null;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

        if (!(startDateString == null) && endDateString == null || startDateString == null && !(endDateString == null)) {
            System.err.println("Error : One of the dates is null");
            throw new WebApplicationException("Error 400 Bad Request : One of the dates is null", Response.Status.BAD_REQUEST);
        } else if (!(startDateString == null && endDateString == null)) {
            try {
                startDate = dateFormat.parse(startDateString);
                endDate = dateFormat.parse(endDateString);
            } catch (ParseException e) {
                System.out.println("Error ParseException : " + e.getMessage());
                throw new WebApplicationException("Error 500 Internal Server Error : " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        // Testing if collection exist
        try {
            collection = ApiApplication.connect("Market", section);
        } catch (IllegalArgumentException e) {
            System.err.println("Error IllegalArgumentException : " + e.getMessage());
            throw new WebApplicationException("Error 404 Not Found : " + e.getMessage(), Response.Status.NOT_FOUND);
        }

        // Testing if collection is empty
        documentFindIterable = collection.find();
        if (!documentFindIterable.iterator().hasNext()) {
            System.err.println("Error : Collection " + section + " is empty");
            throw new WebApplicationException("Error 404 Not Found : Collection " + section + "is empty", Response.Status.NOT_FOUND);
        }

        // Testing if item exist
        document = (Document) documentFindIterable.first().get(item);
        if (document == null) {
            System.err.println("Error : Item " + item + " does not exist in " + section + " Collection");
            throw new WebApplicationException("Error 404 Not Found : Item " + item + " does not exist in " + section + " Collection", Response.Status.NOT_FOUND);
        }
        document.put("date", documentFindIterable.first().getDate("date"));

        return document.toJson();
    }
}