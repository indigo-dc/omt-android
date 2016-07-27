package pl.psnc.indigo.omt;

import java.util.ArrayList;
import org.junit.Test;
import pl.psnc.indigo.omt.api.model.Link;
import pl.psnc.indigo.omt.api.model.MediaType;
import pl.psnc.indigo.omt.api.model.Root;
import pl.psnc.indigo.omt.api.model.Task;
import pl.psnc.indigo.omt.api.model.TaskStatus;
import pl.psnc.indigo.omt.api.model.Version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by michalu on 07.07.16.
 */
public class ModelTests {
    @Test public void testRoot() {
        Root root = new Root();
        root.setLinks(new ArrayList<Link>());
        root.setVersions(new ArrayList<Version>());
        assertEquals("Root[links=[],versions=[]]", root.toString());

        Root other = new Root();
        other.setLinks(root.getLinks());
        other.setVersions(root.getVersions());

        assertEquals(root, root);
        assertEquals(root, other);
        assertEquals(root.hashCode(), other.hashCode());

        assertNotEquals(root, null);
        assertNotEquals(root, "");
    }

    @Test public void testTask() {
        Task task = new Task();
        task.setApplication("app1");
        task.setDate("2016-07-01");
        task.setLast_change("2016-07-14");
        task.setDescription("Lorem");
        task.setId("ID1");
        task.setIosandbox("sandbox1");
        task.setStatus(TaskStatus.ANY);
        task.setCreation("creation");
        task.setUser("michalu");
        assertEquals(
            "Task[id=ID1,date=2016-07-01,lastChange=2016-07-14,application=app1,description=Lorem,status=ANY,user=michalu,creation=creation,iosandbox=sandbox1]",
            task.toString());
    }

    @Test public void testTaskEquals() {
        Task task = new Task();
        assertEquals(task, task);
        assertNotEquals(task, null);
        assertNotEquals(task, "");
    }

    @Test public void testMediaType() {
        MediaType mediaType = new MediaType("application/json");
        assertEquals(mediaType, mediaType);
        assertNotEquals(mediaType, null);
        assertNotEquals(mediaType, "");
    }

    @Test public void testVersion() {
        Version version = new Version();
        version.setStatus("Status");
        version.setUpdated("Updated");
        version.setMediaTypes(new MediaType("application/json"));
        version.setLinks(new ArrayList<Link>());
        version.setId("Id");
        assertEquals("Version[status=Status,updated=Updated,"
            + "mediaType=MediaType[type=application/json],links=[],id=Id]", version.toString());

        Version other = new Version();
        other.setStatus(version.getStatus());
        other.setUpdated(version.getUpdated());
        other.setMediaTypes(version.getMediaTypes());
        other.setLinks(version.getLinks());
        other.setId(version.getId());

        assertEquals(version, version);
        assertEquals(version, other);
        assertEquals(version.hashCode(), other.hashCode());

        assertNotEquals(version, null);
        assertNotEquals(version, "");
    }

    @Test public void testLink() {
        Link link = new Link();
        link.setHref("Href");
        link.setRel("Rel");
        assertEquals("Link[rel=Rel,href=Href]", link.toString());
    }
}
