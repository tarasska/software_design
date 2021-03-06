import org.junit.Assert;
import org.junit.Test;

public class SequentialListTest {
    @Test
    public void sizeTest() {
        SequentialList<String> list = new SequentialList<>();
        Assert.assertEquals("An empty list must not contain any elements", 0, list.size());

        list.addFirst("Taras");
        list.addFirst("Kate");
        list.addFirst("Mike");
        Assert.assertEquals("List should contain 3 elements", 3, list.size());

        list.removeLast();
        list.remove("Mike");
        Assert.assertEquals("List should contain 1 elements", 1, list.size());
    }

    @Test
    public void addRemoveTest() {
        SequentialList<String> list = new SequentialList<>();
        list.addFirst("Taras");
        list.addFirst("Yan");
        list.addFirst("Kate");
        list.addFirst("Mike");

        Assert.assertEquals("Taras should be last person.", "Taras", list.removeLast().getValue());
        Assert.assertTrue("List should contain Kate", list.remove("Kate"));
        Assert.assertFalse("List shouldn't contain the deleted person", list.remove("Kate"));
        Assert.assertEquals("Yan should be last person.", "Yan", list.removeLast().getValue());
    }

    @Test
    public void removeLastTest() {
        SequentialList<Integer> list = new SequentialList<>();
        list.addFirst(0);
        list.addFirst(1);
        list.addFirst(2);
        Assert.assertEquals(3, list.size());

        Assert.assertEquals(0, (int) list.removeLast().getValue());
        Assert.assertEquals(1, (int) list.removeLast().getValue());
        Assert.assertEquals(1, list.size());

        Assert.assertEquals(2, (int) list.removeLast().getValue());
        Assert.assertEquals(0, list.size());
    }

}
