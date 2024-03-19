package com.tobeto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.entity.Box;
import com.tobeto.entity.Fruit;
import com.tobeto.exception.ServiceException;
import com.tobeto.exception.ServiceException.ERROR_CODES;
import com.tobeto.repository.BoxRepository;
import com.tobeto.repository.FruitRepository;

import jakarta.transaction.Transactional;

@Service
public class FruitService {
	@Autowired
	private FruitRepository fruitRepository;
	@Autowired
	private BoxRepository boxRepository;

	public Fruit createFruit(Fruit fruit) {
		return fruitRepository.save(fruit);
	}

	public List<Fruit> getAllFruits() {
		return fruitRepository.findAll();
	}

	public void deleteFruit(int id) {
		fruitRepository.deleteById(id);
	}

	@Transactional
	public void acceptFruit(int fruitId, int count) {
		Fruit fruit = getFruit(fruitId);
		Optional<Box> oBox = boxRepository.findByFruitIdNotFull(fruitId);
		if (oBox.isPresent()) {
			// yarı dolu box bulundu. İçine aldığı kadar fruit koyalım.
			Box box = oBox.get();
			int konacakMiktar = count;
			int boxIcindeKalanKisim = box.getCapacity() - box.getCount();
			if (konacakMiktar > boxIcindeKalanKisim) {
				konacakMiktar = boxIcindeKalanKisim;
			}
			box.setCount(box.getCount() + konacakMiktar);
			boxRepository.save(box);
			count -= konacakMiktar;
		}
		// kalan fruit'ler varsa kalan boş box'lara doldurulacak.
		if (count > 0) {
			bosBoxDoldur(count, fruit);
		}
	}

	/**
	 * @param count
	 * @param fruit
	 */
	private void bosBoxDoldur(int count, Fruit fruit) {
		List<Box> emptyBoxes = boxRepository.findAllByCount(0);
		int siradakiIlkBosSirasi = 0;
		while (count > 0) {
			if (siradakiIlkBosSirasi >= emptyBoxes.size()) {
				// elimizde doldurabileceğimiz boş box kalmadı.
				throw new ServiceException(ERROR_CODES.NOT_ENOUGH_BOX);
			}
			Box box = emptyBoxes.get(siradakiIlkBosSirasi); // ilk boş kutu
			box.setFruit(fruit);
			int konacakMiktar = count;
			if (konacakMiktar > box.getCapacity()) {
				konacakMiktar = box.getCapacity();
			}
			box.setCount(konacakMiktar);
			boxRepository.save(box);
			count -= konacakMiktar;
			siradakiIlkBosSirasi++;
		}
	}

	private Fruit getFruit(int fruitId) {
		Optional<Fruit> oFruit = fruitRepository.findById(fruitId);
		Fruit fruit = null;
		if (oFruit.isPresent()) {
			fruit = oFruit.get();
		} else {
			// fruit bulunamadı. hata ver
			throw new ServiceException(ERROR_CODES.FRUIT_NOT_FOUND);
		}
		return fruit;
	}

	@Transactional
	public void saleFruit(int fruitId, int count) {
		Fruit fruit = getFruit(fruitId);
		Optional<Box> oBox = boxRepository.findByFruitIdNotFull(fruitId);
		if (oBox.isPresent()) {
			// yarı dolu box bulundu. Satış öncelikli olarak bu box içinden yapılacak.
			Box box = oBox.get();
			int satisMiktari = count;

			if (satisMiktari > box.getCount()) {
				satisMiktari = box.getCount();
			}
			box.setCount(box.getCount() - satisMiktari);
			if (box.getCount() == 0) {
				// boş boşaldı. Fruit ile ilişkisini kaldıralım.
				box.setFruit(null);
			}
			boxRepository.save(box);
			count -= satisMiktari;
		}
		// satış yapılacak fruit'ler kaldı ise diğer tam dolu box'lardan satış devam
		// edecek.
		if (count > 0) {
			tamDoluBoxlardanSatisYap(count, fruit);
		}
	}

	private void tamDoluBoxlardanSatisYap(int count, Fruit fruit) {
		List<Box> fullBoxes = boxRepository.findAllByFruitIdAndCountGreaterThan(fruit.getId(), 0);
		int siradakiIlkDoluSirasi = fullBoxes.size() - 1;
		while (count > 0) {
			if (siradakiIlkDoluSirasi < 0) {
				// elimizde satış yapabileğimiz dolu box kalmadı.
				throw new ServiceException(ERROR_CODES.FRUIT_NOT_FOUND);
			}
			Box box = fullBoxes.get(siradakiIlkDoluSirasi); // ilk dolu box

			int satilacakMiktar = count;
			if (satilacakMiktar > box.getCount()) {
				satilacakMiktar = box.getCount();
			}
			box.setCount(box.getCount() - satilacakMiktar);
			if (box.getCount() == 0) {
				// boş boşaldı. Fruit ile ilişkisini kaldıralım.
				box.setFruit(null);
			}
			boxRepository.save(box);
			count -= satilacakMiktar;
			siradakiIlkDoluSirasi--;
		}
	}

	public int getFruitCount(int fruitId) {
		Integer count = boxRepository.getFruitCount(fruitId);
		return count == null ? 0 : count;
	}
}
