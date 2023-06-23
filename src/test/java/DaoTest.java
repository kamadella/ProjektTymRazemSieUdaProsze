import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.h2.engine.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

import wipb.ee.jspdemo.web.dao.UserDao;
import wipb.ee.jspdemo.web.model.Category;
import wipb.ee.jspdemo.web.dao.CategoryDao;
import wipb.ee.jspdemo.web.model.Advertisement;
import wipb.ee.jspdemo.web.dao.AdvertisementDao;
import wipb.ee.jspdemo.web.model.Vser;

@RunWith(MockitoJUnitRunner.class)
public class DaoTest {
    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private TypedQuery<Category> queryMock;

    private CategoryDao categoryDao;

    private AdvertisementDao advertisementDao;
    private UserDao userDao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        categoryDao = new CategoryDao();
        categoryDao.setEm(entityManagerMock);
        advertisementDao = new AdvertisementDao();
        advertisementDao.setEm(entityManagerMock);
        userDao = new UserDao();
        userDao.setEm(entityManagerMock);
    }

    @Test
    public void testSaveOrUpdateCategory() {
        Category category = new Category("Category");


        Category savedCategory = categoryDao.saveOrUpdate(category);

        verify(entityManagerMock).persist(category);
        assertEquals(category, savedCategory);
    }

    @Test
    public void testRemoveCategory() {
        Long categoryId = 1L;

        categoryDao.remove(categoryId);

        verify(entityManagerMock).getReference(Category.class, categoryId);
        verify(entityManagerMock).remove(any());
    }

    @Test
    public void testFindByNameCategory() {
        String categoryName = "Test Category";
        Category category = new Category();
        when(entityManagerMock.createNamedQuery("Category.findByName", Category.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter("name", categoryName)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Arrays.asList(category));

        List<Category> foundCategories = categoryDao.findByName(categoryName);

        assertEquals(1, foundCategories.size());
        assertEquals(category, foundCategories.get(0));
    }

    @Test
    public void testFindAllCategory() {
        Category category1 = new Category();
        Category category2 = new Category();
        when(entityManagerMock.createNamedQuery("Category.findAll", Category.class))
                .thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Arrays.asList(category1, category2));

        List<Category> foundCategories = categoryDao.findAll();

        assertEquals(2, foundCategories.size());
        assertTrue(foundCategories.contains(category1));
        assertTrue(foundCategories.contains(category2));
    }

    @Test
    public void saveOrUpdateAdvertisement(){
        Category category = new Category("Category");
        categoryDao.saveOrUpdate(category);
        Vser u = new Vser("jawel", "123", "paplo@exp.com", "normal");

        Advertisement advertisement = new Advertisement("Title", "Description", category, u);
        Advertisement savedAdvertisement = advertisementDao.saveOrUpdate(advertisement);

        verify(entityManagerMock).persist(advertisement);
        assertEquals(advertisement, savedAdvertisement);

    }

    @Test
    public void removeAdvertisement(){
        Long advertisementId = 1L;
        Category category = new Category("Category");
        categoryDao.saveOrUpdate(category);
        Vser u = new Vser("jawel", "123", "paplo@exp.com", "normal");
        Advertisement advertisement = new Advertisement("Title", "Description", category, u);
        Advertisement savedAdvertisement = advertisementDao.saveOrUpdate(advertisement);
        advertisementDao.remove(advertisementId);
        verify(entityManagerMock).getReference(Advertisement.class, advertisementId);
        verify(entityManagerMock).remove(any());
    }

    @Test
    public void findByIdAdvertisements(){
        Long advertisementId = 1L;
        Category category = new Category("Category");
        categoryDao.saveOrUpdate(category);
        Vser u = new Vser("jawel", "123", "paplo@exp.com", "normal");
        Advertisement advertisement = new Advertisement("Title", "Description", category, u);
        advertisementDao.saveOrUpdate(advertisement);
        when(entityManagerMock.find(Advertisement.class, advertisementId)).thenReturn(advertisement);
        Optional<Advertisement> result = advertisementDao.findById(advertisementId);
        assertTrue(result.isPresent());
        assertEquals(advertisement, result.get());
    }

    @Test
    public void findAllAdvertisement() {
        Category category = new Category("Category");
        categoryDao.saveOrUpdate(category);
        Vser u = new Vser("jawel", "123", "paplo@exp.com", "normal");
        Advertisement advertisement = new Advertisement("Title", "Description", category, u);
        advertisementDao.saveOrUpdate(advertisement);
        Advertisement advertisement2 = new Advertisement("Title2", "Description2", category, u);
        advertisementDao.saveOrUpdate(advertisement);
        List<Advertisement> createdAdvertisement = new ArrayList();
        createdAdvertisement.add(advertisement);
        createdAdvertisement.add(advertisement2);
        TypedQuery<Advertisement> queryMock = mock(TypedQuery.class);
        when(entityManagerMock.createNamedQuery("Advertisement.findAll", Advertisement.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(createdAdvertisement);
        List<Advertisement> foundAdvertisements = advertisementDao.findAll();
        assertEquals(createdAdvertisement, foundAdvertisements);
    }

    @Test
    public void saveOrUpdateVser(){
        Vser user = new Vser("abc", "abc", "abc", "admin");
        Vser savedUser = userDao.saveOrUpdate(user);
        verify(entityManagerMock).persist(user);
        assertEquals(user, savedUser);
    }

    @Test
    public void removeVser(){
        Long userId = 1L;

        userDao.remove(userId);

        verify(entityManagerMock).getReference(Vser.class, userId);
        verify(entityManagerMock).remove(any());
    }
}

