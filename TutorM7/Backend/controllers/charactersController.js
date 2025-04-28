const heroes = require("../heroes.json");
let filteredHeroes = [];
heroes.forEach((hero) => {
  const newFilter = {
    id: hero.id,
    name: hero.name,
    description: hero.description,
    difficulty: hero.difficulty,
    image: hero.image_square,
    deleted_at: null,
  };
  filteredHeroes.push(newFilter);
});

const getAllHeroes = (req, res) => {
  const activeHeroes = filteredHeroes.filter(
    (hero) => hero.deleted_at === null
  );
  return res.status(200).json({
    status: 200,
    heroes: activeHeroes,
    count: activeHeroes.length,
  });
};

const getHeroById = (req, res) => {
  const heroId = req.params.id;
  const hero = filteredHeroes.find(
    (hero) => hero.id === heroId && hero.deleted_at === null
  );
  if (!hero) {
    return res.status(404).json({
      status: 404,
      message: "Hero not found",
    });
  }
  return res.status(200).json({
    status: 200,
    hero,
  });
};

const deleteHero = (req, res) => {
  const heroId = req.params.id;
  const heroIndex = filteredHeroes.findIndex((hero) => hero.id === heroId);
  if (heroIndex === -1) {
    return res.status(404).json({
      status: 404,
      message: "Hero not found",
    });
  }
  filteredHeroes[heroIndex].deleted_at = new Date().toISOString();
  return res.status(200).json({
    status: 200,
    message: "Hero deleted successfully",
  });
};

const getDeletedHeroes = (req, res) => {
  const deletedHeroes = filteredHeroes.filter(
    (hero) => hero.deleted_at !== null
  );
  return res.status(200).json({
    status: 200,
    heroes: deletedHeroes,
    count: deletedHeroes.length,
  });
};


const updateHero = (req, res) => {
  const heroId = req.params.id;
  const heroIndex = filteredHeroes.findIndex((hero) => hero.id === heroId);
  if (heroIndex === -1) {
    return res.status(404).json({
      status: 404,
      message: "Hero not found",
    });
  }

  const { name, description, difficulty } = req.body;

  filteredHeroes[heroIndex].name = name || filteredHeroes[heroIndex].name;
  filteredHeroes[heroIndex].description =
    description || filteredHeroes[heroIndex].description;
  filteredHeroes[heroIndex].difficulty =
    difficulty || filteredHeroes[heroIndex].difficulty;

  return res.status(200).json({
    status: 200,
    message: "Hero updated successfully",
    hero: filteredHeroes[heroIndex],
  });
};

module.exports = {
  getAllHeroes,
  getHeroById,
  deleteHero,
  getDeletedHeroes,
  updateHero,
};
